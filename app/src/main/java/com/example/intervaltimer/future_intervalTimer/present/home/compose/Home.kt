@file:OptIn(ExperimentalAnimationApi::class)

package com.example.intervaltimer.future_intervalTimer.present

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SurroundSound
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.intervaltimer.R
import com.example.intervaltimer.core.enums.Option
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel
import com.example.intervaltimer.future_intervalTimer.present.util.screen.Screen
import com.example.intervaltimer.R.drawable.ic_play
import com.example.intervaltimer.core.Event.UiEvent
import com.example.intervaltimer.future_intervalTimer.domain.model.ChooseOptionEnum
import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import com.example.intervaltimer.future_intervalTimer.present.home.HomeEvent
import com.example.intervaltimer.future_intervalTimer.present.home.compose.timerOption
import com.example.intervaltimer.future_intervalTimer.present.home.HomeViewModel
import com.example.intervaltimer.future_intervalTimer.present.home.compose.CurrentChoosePresentation
import com.maxkeppeker.sheets.core.models.base.BaseSelection
import com.maxkeppeker.sheets.core.models.base.ButtonStyle
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.duration.DurationDialog
import com.maxkeppeler.sheets.duration.models.DurationConfig
import com.maxkeppeler.sheets.duration.models.DurationFormat
import com.maxkeppeler.sheets.duration.models.DurationSelection
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(
    navHostController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state.value

    val timerState = rememberUseCaseState()
    val roundState = rememberUseCaseState()
    var option: ChooseOptionEnum? = null

    DurationDialog(
        state = timerState,
        selection = DurationSelection(
            positiveButton = SelectionButton(
                text = "Apply"
            ),
            onPositiveClick = {
                when(option) {
                    ChooseOptionEnum.PrepareTime -> {
                        viewModel.onEvent(HomeEvent.setPrepareTime(it))
                    }
                    ChooseOptionEnum.RoundTime -> {
                        viewModel.onEvent(HomeEvent.setRoundTime(it))
                    }
                    ChooseOptionEnum.BreakTime -> {
                        viewModel.onEvent(HomeEvent.setBreakTime(it))
                    }
                    null -> { }
                }
                option = null
            }
        ),
        config = DurationConfig(
            timeFormat = DurationFormat.MM_SS
        )
    )

    DurationDialog(
        state = roundState,
        selection = DurationSelection(
            positiveButton = SelectionButton(
                text = "Apply"
            ),
            onPositiveClick = {
                viewModel.onEvent(HomeEvent.setRounds(it))
            }
        ),
        config = DurationConfig(
            timeFormat = DurationFormat.SS
        )
    )

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    viewModel.onEvent(HomeEvent.InsertOwnIntervalTime)
                }
            ) {
                AnimatedContent(
                    targetState = state.timerExist,
                    transitionSpec = {
                        fadeIn() with fadeOut()
                    }
                ) {
                    if (it == true) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "Like",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .size(40.dp)

                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.StarBorder,
                            contentDescription = "Like",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .size(40.dp)

                        )
                    }
                }
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.sound_sampler),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .size(200.dp)
                    .clickable {
                        navHostController.navigate(
                            Screen.Timer.sendData(
                                state.timer
                            )
                        )
                    }
            )

            CurrentChoosePresentation(
                time = state.timer.startTime,
                text = "Time to prepare"
            ) {
                option = ChooseOptionEnum.PrepareTime
                timerState.show()
            }
            CurrentChoosePresentation(
                time = state.timer.roundTime,
                text = "Round Time"
            ) {
                option = ChooseOptionEnum.RoundTime
                timerState.show()
            }
            CurrentChoosePresentation(
                time = state.timer.delay,
                text = "Break Time"
            ) {
                option = ChooseOptionEnum.BreakTime
                timerState.show()
            }
            CurrentChoosePresentation(
                time = state.timer.rounds,
                text = "Rounds"
            ) {
                timerState.show()
            }

            Spacer(modifier = Modifier.height(50.dp))
            Button(
                onClick = {
                    navHostController.navigate(Screen.OwnIntervalTimers.route)
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                Text(
                    text = "Own Intervals"
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    navHostController.navigate(Screen.History.route)
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                Text(
                    text = "History"
                )
            }
        }
    }
}