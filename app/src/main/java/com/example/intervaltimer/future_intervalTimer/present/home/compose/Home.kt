package com.example.intervaltimer.future_intervalTimer.present

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.intervaltimer.core.enums.Option
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel
import com.example.intervaltimer.future_intervalTimer.present.util.screen.Screen
import com.example.intervaltimer.R.drawable.ic_play
import com.example.intervaltimer.core.Event.UiEvent
import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import com.example.intervaltimer.future_intervalTimer.present.home.HomeEvent
import com.example.intervaltimer.future_intervalTimer.present.home.compose.timerOption
import com.example.intervaltimer.future_intervalTimer.present.home.HomeViewModel
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navHostController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state.value

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
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Default.Add),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp)
                        .clickable {
                            viewModel.onEvent(HomeEvent.InsertOwnIntervalTime)
                        }
                )
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = ic_play),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(100.dp)
                        .clickable {
                            navHostController.navigate(
                                Screen.Timer.sendData(
                                    TimerModel(
                                        startTime = state.timeToPrepare,
                                        rounds = state.rounds,
                                        delay = state.breakTime,
                                        roundTime = state.roundTime
                                    )
                                )
                            )
                        }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Time to prepare"
            )
            Text(
                text = "${state.timeToPrepare/60}:${if((state.timeToPrepare%60) < 10) "0" + state.timeToPrepare%60 else state.timeToPrepare%60}",
                modifier = Modifier
                    .clickable {

                    }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Round Time"
            )
            Text(
                text = "${state.roundTime/60}:${if((state.roundTime%60) < 10) "0" + state.roundTime%60 else state.roundTime%60}",
                modifier = Modifier
                    .clickable {

                    }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Break Time"
            )
            Text(
                text = "${state.breakTime/60}:${if((state.breakTime%60) < 10) "0" + state.breakTime%60 else state.breakTime%60}",
                modifier = Modifier
                    .clickable {

                    }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Rounds"
            )
            Text(
                text = "${state.rounds}",
                modifier = Modifier
                    .clickable {
                        
                    }
            )

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