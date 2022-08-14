package com.example.intervaltimer.future_intervalTimer.present.history.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.intervaltimer.future_intervalTimer.present.history.HistoryViewModel

@Composable
fun HistoryPresent(
    navHostController: NavHostController,
    viewModel: HistoryViewModel = hiltViewModel()
) {

    val intervalTimes = viewModel.state.value.intervalTimes

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "History",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Left
        )

        Spacer(modifier = Modifier.height(16.dp))

        if(intervalTimes.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(intervalTimes) { intervatTimer ->
                    HistoryItem(
                        navHostController = navHostController,
                        timer = intervatTimer.toTimer()
                    )
                }
            }
        } else {
            Text(
                text = "History is empty!",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }

}