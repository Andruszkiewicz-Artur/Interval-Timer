package com.example.intervaltimer.Present

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.intervaltimer.Model.Option
import com.example.intervaltimer.Model.TimerModel
import com.example.intervaltimer.Navigation.Screen
import com.example.intervaltimer.View.TextField
import com.example.intervaltimer.R.drawable.ic_play

@Composable
fun Home(
    navHostController: NavHostController
) {
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
            Icon(
                painter = painterResource(id = ic_play),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        navHostController.navigate(Screen.Timer.sendData(
                            TimerModel(
                                startTime = (startTimeMin * 60) + startTimeSec,
                                rounds = rounds,
                                delay = (delayMin * 60) + delaySec,
                                roundTime = (roundMin * 60) + roundSec
                            )
                        ))
                    }
            )
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
                        Text(text = "Start Time")
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextField(
                                title = "Min",
                                text = startTimeMin,
                                value = {
                                    startTimeMin = it
                                }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            TextField(
                                title = "Sec",
                                text = startTimeSec,
                                value = {
                                    startTimeSec = it
                                }
                            )
                        }
                    }
                    Option.Round_Time -> {
                        Text(text = "Round Time")
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextField(
                                title = "Min",
                                text = roundMin,
                                value = {
                                    roundMin = it
                                }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            TextField(
                                title = "Sec",
                                text = roundSec,
                                value = {
                                    roundSec = it
                                }
                            )
                        }
                    }
                    Option.Delay -> {
                        Text(text = "Delay Time")
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextField(
                                title = "Min",
                                text = delayMin,
                                value = {
                                    delayMin = it
                                }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            TextField(
                                title = "Sec",
                                text = delaySec,
                                value = {
                                    delaySec = it
                                }
                            )
                        }
                    }
                    Option.Rounds -> {
                        Text(text = "Rounds")
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextField(
                                title = "How many Rounds",
                                text = rounds,
                                value = {
                                    rounds = it
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}