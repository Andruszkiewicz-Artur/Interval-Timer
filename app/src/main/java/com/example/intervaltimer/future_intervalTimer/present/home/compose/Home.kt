@file:OptIn(ExperimentalAnimationApi::class)

package com.example.intervaltimer.future_intervalTimer.present

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.intervaltimer.R
import com.example.intervaltimer.future_intervalTimer.present.util.screen.Screen
import com.example.intervaltimer.core.Event.UiEvent
import com.example.intervaltimer.core.constants.Constants
import com.example.intervaltimer.future_intervalTimer.domain.model.ChooseOptionEnum
import com.example.intervaltimer.future_intervalTimer.domain.service.ServiceHelper
import com.example.intervaltimer.future_intervalTimer.present.home.HomeEvent
import com.example.intervaltimer.future_intervalTimer.present.home.HomeViewModel
import com.example.intervaltimer.future_intervalTimer.present.home.compose.CurrentChoosePresentation
import com.example.intervaltimer.future_intervalTimer.present.home.compose.HomeButton
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
            negativeButton = null,
            positiveButton = SelectionButton(
                text = stringResource(id = R.string.Apply)
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
            negativeButton = null,
            positiveButton = SelectionButton(
                text = stringResource(id = R.string.Apply)
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
                    Toast.makeText(
                        context,
                        context.getString(event.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        ServiceHelper.triggerForegroundService(
            context = context,
            action = Constants.ACTION_SERVICE_START
        )
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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                HomeButton(
                    image = Icons.Filled.Timer,
                    text = stringResource(id = R.string.OwnTimers)
                ) {
                    navHostController.navigate(Screen.OwnIntervalTimers.route)
                }

                Spacer(modifier = Modifier.fillMaxWidth(0.6f))

                HomeButton(
                    image = Icons.Filled.History,
                    text = stringResource(id = R.string.History)
                ) {
                    navHostController.navigate(Screen.History.route)
                }
            }

            Text(
                text = stringResource(id = R.string.IntervalTimer),
                fontSize = MaterialTheme.typography.displayLarge.fontSize,
                modifier = Modifier
                    .padding(16.dp)
            )

            CurrentChoosePresentation(
                time = state.timer.startTime,
                text = stringResource(id = R.string.TimeToPrepare)
            ) {
                option = ChooseOptionEnum.PrepareTime
                timerState.show()
            }
            CurrentChoosePresentation(
                time = state.timer.roundTime,
                text = stringResource(id = R.string.RoundTime)
            ) {
                option = ChooseOptionEnum.RoundTime
                timerState.show()
            }
            CurrentChoosePresentation(
                time = state.timer.delay,
                text = stringResource(id = R.string.BreakTime)
            ) {
                option = ChooseOptionEnum.BreakTime
                timerState.show()
            }
            CurrentChoosePresentation(
                time = state.timer.rounds,
                text = stringResource(id = R.string.Rounds),
                isTimer = false
            ) {
                roundState.show()
            }
            Spacer(modifier = Modifier.height(40.dp))
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
        }
    }
}