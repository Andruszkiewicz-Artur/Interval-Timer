package com.example.intervaltimer.future_intervalTimer.present

import TimerGraphPresentation
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.intervaltimer.R
import com.example.intervaltimer.core.global.globalTimer
import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTimeState
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel
import com.example.intervaltimer.future_intervalTimer.domain.service.IntervalTimeService
import com.example.intervaltimer.future_intervalTimer.present.timer.TimerEvent
import com.example.intervaltimer.future_intervalTimer.present.timer.TimerStateEnum
import com.example.intervaltimer.future_intervalTimer.present.timer.TimerUiEvent
import com.example.intervaltimer.future_intervalTimer.present.timer.TimerViewModel
import com.example.intervaltimer.future_intervalTimer.present.timer.compose.ButtonTimerPresentation
import com.example.intervaltimer.ui.theme.Green
import com.example.intervaltimer.ui.theme.Purple
import com.example.intervaltimer.ui.theme.Red
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.DurationUnit

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("AutoboxingStateCreation")
@Composable
fun TimerPresentation(
    navHostController: NavHostController,
    timer: TimerModel,
    viewModel: TimerViewModel = hiltViewModel(),
    service: IntervalTimeService
) {
    val state = viewModel.state.value
    val round = service.round.value
    val roundState = service.status
    val duration = service.duration
    val stateTimer = service.currentState.value

    LaunchedEffect(key1 = true) {
        viewModel.setupTimer(timer)
        viewModel.shareFlow.collectLatest { event ->
            when(event) {
                is TimerUiEvent.Finish -> {
                    navHostController.popBackStack()
                }
            }
        }
    }

    LaunchedEffect(key1 = stateTimer) {
        if (stateTimer == IntervalTimeState.Canceled) {
            navHostController.popBackStack()
        }
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        TimerGraphPresentation(
            indicatorValue = duration.toInt(DurationUnit.SECONDS),
            maxIndicatorValue = when (roundState) {
                TimerStateEnum.Preparing -> globalTimer.startTime
                TimerStateEnum.Round -> globalTimer.roundTime
                TimerStateEnum.Break -> globalTimer.delay
            },
            smallText = "${round}/${globalTimer.rounds}",
            canvasSize = 400.dp,
            foregroundIndicatorColor = when (roundState) {
                TimerStateEnum.Preparing -> Purple
                TimerStateEnum.Round -> Green
                TimerStateEnum.Break -> Red
            },
            foregroundIndicatorStrokeWidth = 70f
        )

        AnimatedContent(
            targetState = roundState
        ) {
            when(it) {
                TimerStateEnum.Preparing -> {
                    Text(
                        text = stringResource(id = R.string.Preparing),
                        fontSize = MaterialTheme.typography.h2.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                }
                TimerStateEnum.Round -> {
                    Text(
                        text = stringResource(id = R.string.Round),
                        fontSize = MaterialTheme.typography.h2.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                }
                TimerStateEnum.Break -> {
                    Text(
                        text = stringResource(id = R.string.Break),
                        fontSize = MaterialTheme.typography.h2.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))

        AnimatedContent(
            targetState = stateTimer == IntervalTimeState.Stopped,
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(16.dp)
                .height(100.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!it) {
                    ButtonTimerPresentation(
                        icon = Icons.Filled.Block
                    ) {
                        viewModel.onEvent(TimerEvent.Cancel)
                    }
                    Spacer(modifier = Modifier.fillMaxWidth(0.3f))
                    ButtonTimerPresentation(
                        icon = Icons.Filled.Pause
                    ) {
                        viewModel.onEvent(TimerEvent.Stop)
                    }
                } else {
                    ButtonTimerPresentation(
                        icon = Icons.Filled.PlayArrow
                    ) {
                        viewModel.onEvent(TimerEvent.Start)
                    }
                }
            }
        }
    }
}