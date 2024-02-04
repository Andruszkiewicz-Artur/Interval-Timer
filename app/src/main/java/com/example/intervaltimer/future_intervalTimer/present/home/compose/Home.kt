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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.intervaltimer.R
import com.example.intervaltimer.future_intervalTimer.present.util.navigation.screen.Screen
import com.example.intervaltimer.core.Event.UiEvent
import com.example.intervaltimer.core.constants.Constants
import com.example.intervaltimer.core.global.globalTimer
import com.example.intervaltimer.future_intervalTimer.domain.model.ChooseOptionEnum
import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTimeState
import com.example.intervaltimer.future_intervalTimer.domain.service.IntervalTimeService
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(
    navHostController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
    service: IntervalTimeService
) {
    val context = LocalContext.current
    val state = viewModel.state.value

    val timerState = rememberUseCaseState()
    val roundState = rememberUseCaseState()
    var option: ChooseOptionEnum? = null
    val currentState = service.state.value.currentState
    val menuState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = currentState) {
        Log.d("Check current state", "${currentState != IntervalTimeState.Idle && currentState != IntervalTimeState.Canceled}")
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


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Text(
                        text = stringResource(id = R.string.Menu),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(16.dp)
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    NavigationDrawerItem(
                        label = {
                            Text(text = stringResource(id = R.string.OwnTimers))
                        },
                        onClick = {
                            navHostController.navigate(Screen.OwnIntervalTimers.route)
                            scope.launch {
                                menuState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Timer,
                                contentDescription = null
                            )
                        },
                        selected = false,
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = {
                            Text(text = stringResource(id = R.string.History))
                        },
                        onClick = {
                            navHostController.navigate(Screen.History.route)
                            scope.launch {
                                menuState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.History,
                                contentDescription = null
                            )
                        },
                        selected = false,
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            },
            drawerState = menuState
        ) {
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
                                    text = "8:40",
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
                                    menuState.open()
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
                    }
                }
            }
        }
    }
}