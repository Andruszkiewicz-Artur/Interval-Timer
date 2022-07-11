package com.example.intervaltimer.future_intervalTimer.present

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import com.example.intervaltimer.future_intervalTimer.present.home.compose.timerOption
import com.example.intervaltimer.future_intervalTimer.present.home.HomeViewModel

@Composable
fun Home(
    navHostController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var startTimeMin by remember { mutableStateOf(0) }
    var startTimeSec by remember { mutableStateOf(15) }
    var roundMin by remember { mutableStateOf(3) }
    var roundSec by remember { mutableStateOf(0) }
    var delayMin by remember { mutableStateOf(1) }
    var delaySec by remember { mutableStateOf(0) }
    var rounds by remember { mutableStateOf(5) }
    var option by remember { mutableStateOf<Option?>(null) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
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
                                        startTime = (startTimeMin * 60) + startTimeSec,
                                        rounds = rounds,
                                        delay = (delayMin * 60) + delaySec,
                                        roundTime = (roundMin * 60) + roundSec
                                    )
                                )
                            )
                        }
                )
                Spacer(modifier = Modifier.width(20.dp))
                Icon(
                    painter = rememberVectorPainter(image = Icons.Filled.AddCircle),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            val isInsert = viewModel.insertOwnIntervalTime(
                                TimerModel(
                                    startTime = (startTimeMin * 60) + startTimeSec,
                                    rounds = rounds,
                                    delay = (delayMin * 60) + delaySec,
                                    roundTime = (roundMin * 60) + roundSec
                                )
                            )

                            Toast
                                .makeText(
                                    context,
                                    if (isInsert) "You create new Own Interval Timer"
                                    else "You have this timer",
                                    Toast.LENGTH_LONG
                                )
                                .show()

                        }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Time to prepare"
            )
            Text(
                text = "$startTimeMin:${if(startTimeSec < 10) "0" + startTimeSec else startTimeSec}",
                modifier = Modifier
                    .clickable {
                        option = Option.Start_Time
                    }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Round Time"
            )
            Text(
                text = "$roundMin:${if(roundSec < 10) "0" + roundSec else roundSec}",
                modifier = Modifier
                    .clickable {
                        option = Option.Round_Time
                    }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Break Time"
            )
            Text(
                text = "$delayMin:${if(delaySec < 10) "0" + delaySec else delaySec}",
                modifier = Modifier
                    .clickable {
                        option = Option.Delay
                    }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Rounds"
            )
            Text(
                text = "$rounds",
                modifier = Modifier
                    .clickable {
                        option = Option.Rounds
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

    if(option != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Transparent
                )
                .clickable {
                    option = null
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp)
            ) {
                when(option) {
                    Option.Start_Time -> {
                        timerOption(
                            title = "Time to prepare",
                            firstField = Pair("Min", startTimeMin),
                            secondField = Pair("Sec", startTimeSec),
                            value = { min, sec ->
                                startTimeMin = min
                                startTimeSec = sec ?: 0
                            }
                        )
                    }
                    Option.Round_Time -> {
                        timerOption(
                            title = "Round Time",
                            firstField = Pair("Min", roundMin),
                            secondField = Pair("Sec", roundSec),
                            value = { min, sec ->
                                roundMin = min
                                roundSec = sec ?: 0
                            }
                        )
                    }
                    Option.Delay -> {
                        timerOption(
                            title = "Break Time",
                            firstField = Pair("Min", delayMin),
                            secondField = Pair("Sec", delaySec),
                            value = { min, sec ->
                                delayMin = min
                                delaySec = sec ?: 0
                            }
                        )
                    }
                    Option.Rounds -> {
                        timerOption(
                            title = "Rounds",
                            firstField = Pair("How many Rounds", rounds),
                            value = { comingRounds, _ ->
                                rounds = comingRounds
                            }
                        )
                    }
                }
            }
        }
    }
}