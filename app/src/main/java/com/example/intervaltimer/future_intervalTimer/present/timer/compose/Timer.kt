package com.example.intervaltimer.future_intervalTimer.present

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.intervaltimer.core.enums.Option
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel
import com.example.intervaltimer.R.drawable.ic_play
import com.example.intervaltimer.R.drawable.ic_pause
import com.example.intervaltimer.ui.theme.Green
import com.example.intervaltimer.ui.theme.Red
import com.example.intervaltimer.ui.theme.Yellow
import kotlinx.coroutines.delay
import com.example.intervaltimer.R.raw.gong
import com.example.intervaltimer.R.raw.bell_finish
import com.example.intervaltimer.R.raw.bell_soon
import com.example.intervaltimer.core.Event.UiEvent
import com.example.intervaltimer.future_intervalTimer.present.timer.TimerViewModel
import com.example.intervaltimer.future_intervalTimer.present.timer.compose.ButtonTimerPresentation
import com.example.intervaltimer.ui.theme.Blue50
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("AutoboxingStateCreation")
@Composable
fun Timer(
    navHostController: NavHostController,
    timer: TimerModel,
    viewModel: TimerViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.shareFlow.collectLatest { event ->
            when(event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
            ) {
                ButtonTimerPresentation(
                    icon = Icons.Filled.Block
                ) {

                }
                ButtonTimerPresentation(
                    icon = Icons.Filled.Pause
                ) {

                }
            }
        }
    }
}