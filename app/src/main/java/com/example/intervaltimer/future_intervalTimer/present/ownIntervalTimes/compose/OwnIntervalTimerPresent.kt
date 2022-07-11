package com.example.intervaltimer.future_intervalTimer.present.ownIntervalTimes.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.intervaltimer.future_intervalTimer.present.ownIntervalTimes.ownIntervalTimesViewModel

@Composable
fun OwnIntervalTimePresent(
    navHostController: NavHostController,
    viewModel: ownIntervalTimesViewModel = hiltViewModel()
) {

    val state = viewModel.state.value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your own Interval Times",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            textAlign = TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.ownIntervalTimes) { ownIntervalTime ->
                OwnIntervalTimesItem(
                    navHostController = navHostController,
                    ownIntervalTime = ownIntervalTime,
                    viewModel = viewModel
                )
            }
        }
    }

}