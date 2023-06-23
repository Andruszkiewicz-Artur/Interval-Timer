package com.example.intervaltimer.future_intervalTimer.present

import TimerGraphPresentation
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel
import com.example.intervaltimer.future_intervalTimer.present.timer.TimerEvent
import com.example.intervaltimer.future_intervalTimer.present.timer.TimerStateEnum
import com.example.intervaltimer.future_intervalTimer.present.timer.TimerUiEvent
import com.example.intervaltimer.future_intervalTimer.present.timer.TimerViewModel
import com.example.intervaltimer.future_intervalTimer.present.timer.compose.ButtonTimerPresentation
import com.example.intervaltimer.ui.theme.Green
import com.example.intervaltimer.ui.theme.Purple
import com.example.intervaltimer.ui.theme.Red
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("AutoboxingStateCreation")
@Composable
fun TimerPresentation(
    navHostController: NavHostController,
    timer: TimerModel,
    viewModel: TimerViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

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


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        TimerGraphPresentation(
            indicatorValue = state.currentTime/10,
            maxIndicatorValue = when (state.currentStatus) {
                TimerStateEnum.Preparing -> timer.startTime
                TimerStateEnum.Round -> timer.roundTime
                TimerStateEnum.Break -> timer.delay
            },
            smallText = "${state.currentRound}/${timer.rounds}",
            canvasSize = 400.dp,
            foregroundIndicatorColor = when (state.currentStatus) {
                TimerStateEnum.Preparing -> Purple
                TimerStateEnum.Round -> Green
                TimerStateEnum.Break -> Red
            },
            foregroundIndicatorStrokeWidth = 70f
        )

        AnimatedContent(
            targetState = state.currentStatus
        ) {
            when(it) {
                TimerStateEnum.Preparing -> {
                    Text(
                        text = "Preparing",
                        fontSize = MaterialTheme.typography.h2.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                }
                TimerStateEnum.Round -> {
                    Text(
                        text = "Round",
                        fontSize = MaterialTheme.typography.h2.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                }
                TimerStateEnum.Break -> {
                    Text(
                        text = "Break",
                        fontSize = MaterialTheme.typography.h2.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))

        AnimatedContent(
            targetState = state.isStop,
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
                        navHostController.popBackStack()
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