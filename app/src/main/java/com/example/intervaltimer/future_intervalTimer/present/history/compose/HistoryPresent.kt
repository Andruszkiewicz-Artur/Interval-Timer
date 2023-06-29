package com.example.intervaltimer.future_intervalTimer.present.history.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.intervaltimer.future_intervalTimer.present.history.HistoryViewModel
import com.example.intervaltimer.R
import com.example.intervaltimer.core.constants.Constants
import com.example.intervaltimer.core.global.globalTimer
import com.example.intervaltimer.future_intervalTimer.domain.service.ServiceHelper
import com.example.intervaltimer.future_intervalTimer.present.util.compose.ItemPresentation

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HistoryPresent(
    navHostController: NavHostController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val intervalTimes = viewModel.state.value.intervalTimes

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.History),
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
                    ItemPresentation(
                        timer = intervatTimer.toTimer(),
                        isDelete = false,
                        onClickStart = {
                            globalTimer = intervatTimer.toTimer()
                            ServiceHelper.triggerForegroundService(
                                context = context,
                                action = Constants.ACTION_SERVICE_START
                            )
                            navHostController.popBackStack()
                        }
                    )
                }
            }
        } else {
            Text(
                text = stringResource(id = R.string.HistoryIsEmpty),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }

}