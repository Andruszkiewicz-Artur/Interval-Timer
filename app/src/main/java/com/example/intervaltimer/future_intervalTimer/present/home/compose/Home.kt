@file:OptIn(ExperimentalAnimationApi::class)

package com.example.intervaltimer.future_intervalTimer.present

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.intervaltimer.R
import com.example.intervaltimer.future_intervalTimer.present.util.navigation.screen.Screen
import com.example.intervaltimer.core.Event.UiEvent
import com.example.intervaltimer.core.constants.Constants
import com.example.intervaltimer.core.extension.toTime
import com.example.intervaltimer.core.global.globalTimer
import com.example.intervaltimer.future_intervalTimer.domain.model.ChooseOptionEnum
import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTimeState
import com.example.intervaltimer.future_intervalTimer.domain.service.IntervalTimeService
import com.example.intervaltimer.future_intervalTimer.domain.service.ServiceHelper
import com.example.intervaltimer.future_intervalTimer.present.home.ChangeTimerValueEnum
import com.example.intervaltimer.future_intervalTimer.present.home.HomeEvent
import com.example.intervaltimer.future_intervalTimer.present.home.HomeViewModel
import com.example.intervaltimer.future_intervalTimer.present.home.TimerOptionEnum
import com.example.intervaltimer.future_intervalTimer.present.home.compose.CurrentChoosePresentation
import com.example.intervaltimer.future_intervalTimer.present.home.compose.HomeButton
import com.example.intervaltimer.future_intervalTimer.present.util.compose.OwnNavigationDrawer
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.duration.DurationDialog
import com.maxkeppeler.sheets.duration.models.DurationConfig
import com.maxkeppeler.sheets.duration.models.DurationFormat
import com.maxkeppeler.sheets.duration.models.DurationSelection
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalComposeUiApi::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(
    navHostController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
    service: IntervalTimeService,
    drawerState: DrawerState
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequesterManager = LocalFocusManager.current

    val context = LocalContext.current
    val state = viewModel.state.collectAsState().value

    val timerState = rememberUseCaseState()
    val roundState = rememberUseCaseState()
    var option: ChooseOptionEnum? = null
    val currentState = service.state.value.currentState
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = currentState) {
        Log.d(
            "Check current state",
            "${currentState != IntervalTimeState.Idle && currentState != IntervalTimeState.Canceled}"
        )
        if (currentState != IntervalTimeState.Idle && currentState != IntervalTimeState.Canceled) {
            navHostController.navigate(Screen.Timer.route)
        }
    }

    DurationDialog(
        state = timerState,
        selection = DurationSelection(
            negativeButton = null,
            positiveButton = SelectionButton(
                text = stringResource(id = R.string.Apply)
            ),
            onPositiveClick = {
                when (option) {
                    ChooseOptionEnum.PrepareTime -> {
                        viewModel.onEvent(HomeEvent.SetValue(TimerOptionEnum.Prepare, it))
                    }

                    ChooseOptionEnum.RoundTime -> {
                        viewModel.onEvent(HomeEvent.SetValue(TimerOptionEnum.RoundTime, it))
                    }

                    ChooseOptionEnum.BreakTime -> {
                        viewModel.onEvent(HomeEvent.SetValue(TimerOptionEnum.Break, it))
                    }
                    else -> {}
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
                viewModel.onEvent(HomeEvent.SetValue(TimerOptionEnum.Break, it))
            }
        ),
        config = DurationConfig(
            timeFormat = DurationFormat.SS
        )
    )

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        context.getString(event.message) + "!",
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


    OwnNavigationDrawer(
        navHostController = navHostController,
        menuState = drawerState,
        content = {
            Scaffold(
                floatingActionButton = {
                    Button(
                        onClick = {
                            globalTimer = state.timer
                            ServiceHelper.triggerForegroundService(
                                context = context,
                                action = Constants.ACTION_SERVICE_START
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(id = R.string.StartWorkout),
                                style = MaterialTheme.typography.titleSmall
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = state.overallTime.toTime(),
                                    style = MaterialTheme.typography.titleSmall
                                )

                                Text(
                                    text = stringResource(id = R.string.Duration),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                },
                floatingActionButtonPosition = FabPosition.Center,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(text = stringResource(id = R.string.IntervalTimer))
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            AnimatedContent(
                                targetState = state.timerExist,
                                transitionSpec = {
                                    fadeIn() with fadeOut()
                                },
                                label = ""
                            ) {
                                if (it == true) {
                                    IconButton(onClick = {
                                        viewModel.onEvent(HomeEvent.InsertOwnIntervalTime)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Bookmark,
                                            contentDescription = "Like",
                                            tint = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier
                                                .size(40.dp)

                                        )
                                    }
                                } else {
                                    IconButton(onClick = {
                                        viewModel.onEvent(HomeEvent.InsertOwnIntervalTime)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.BookmarkBorder,
                                            contentDescription = "Like",
                                            tint = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier
                                                .size(40.dp)

                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            ) { padding ->
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    item {
                        OutlinedTextField(
                            value = state.timer.name,
                            onValueChange = { newName ->
                                viewModel.onEvent(HomeEvent.SetName(newName))
                            },
                            singleLine = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(text = stringResource(id = R.string.Name))
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    focusRequesterManager.clearFocus()
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                        )

                        CurrentChoosePresentation(
                            time = state.timer.startTime,
                            text = stringResource(id = R.string.TimeToPrepare),
                            onClickMinus = {
                                viewModel.onEvent(HomeEvent.ChangeTimerValue(TimerOptionEnum.Prepare, ChangeTimerValueEnum.Subtract))
                            },
                            onClickPlus = {
                                viewModel.onEvent(HomeEvent.ChangeTimerValue(TimerOptionEnum.Prepare, ChangeTimerValueEnum.Add))
                            },
                            onClick = {
                                option = ChooseOptionEnum.PrepareTime
                                timerState.show()
                            }
                        )
                        CurrentChoosePresentation(
                            time = state.timer.roundTime,
                            text = stringResource(id = R.string.RoundTime),
                            onClickMinus = {
                                viewModel.onEvent(HomeEvent.ChangeTimerValue(TimerOptionEnum.RoundTime, ChangeTimerValueEnum.Subtract))
                            },
                            onClickPlus = {
                                viewModel.onEvent(HomeEvent.ChangeTimerValue(TimerOptionEnum.RoundTime, ChangeTimerValueEnum.Add))
                            },
                            onClick = {
                                option = ChooseOptionEnum.RoundTime
                                timerState.show()
                            }
                        )
                        CurrentChoosePresentation(
                            time = state.timer.delay,
                            text = stringResource(id = R.string.BreakTime),
                            onClickMinus = {
                                viewModel.onEvent(HomeEvent.ChangeTimerValue(TimerOptionEnum.Break, ChangeTimerValueEnum.Subtract))
                            },
                            onClickPlus = {
                                viewModel.onEvent(HomeEvent.ChangeTimerValue(TimerOptionEnum.Break, ChangeTimerValueEnum.Add))
                            },
                            onClick = {
                                option = ChooseOptionEnum.BreakTime
                                timerState.show()
                            }
                        )
                        CurrentChoosePresentation(
                            time = state.timer.rounds,
                            text = stringResource(id = R.string.Rounds),
                            isTimer = false,
                            onClickMinus = {
                                viewModel.onEvent(HomeEvent.ChangeTimerValue(TimerOptionEnum.Rounds, ChangeTimerValueEnum.Subtract))
                            },
                            onClickPlus = {
                                viewModel.onEvent(HomeEvent.ChangeTimerValue(TimerOptionEnum.Rounds, ChangeTimerValueEnum.Add))
                            },
                            onClick = {
                                roundState.show()
                            }
                        )

                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    )
}