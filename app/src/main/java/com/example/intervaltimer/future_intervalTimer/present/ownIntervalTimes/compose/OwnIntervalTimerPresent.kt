package com.example.intervaltimer.future_intervalTimer.present.ownIntervalTimes.compose

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.intervaltimer.core.Event.UiEvent
import com.example.intervaltimer.future_intervalTimer.present.ownIntervalTimes.ownIntervalTimesViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun OwnIntervalTimePresent(
    navHostController: NavHostController,
    viewModel: ownIntervalTimesViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val state = viewModel.state.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is UiEvent.ShowToast -> {
                    System.out.println(event.message)
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your own Interval Times",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Left
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