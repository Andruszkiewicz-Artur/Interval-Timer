package com.example.intervaltimer.future_intervalTimer.present.history.compose

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.intervaltimer.core.constants.Constants
import com.example.intervaltimer.core.global.globalTimer
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel
import com.example.intervaltimer.future_intervalTimer.domain.service.ServiceHelper
import com.example.intervaltimer.future_intervalTimer.present.util.screen.Screen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HistoryItem(
    navHostController: NavHostController,
    timer: TimerModel,
    context: Context
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${timer.startTime/60}:${if(timer.startTime%60 < 10) "0" + timer.startTime%60 else timer.startTime%60}",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(0.2f),
            color = MaterialTheme.colorScheme.primary

        )
        Text(
            text = "${timer.roundTime/60}:${if(timer.roundTime%60 < 10) "0" + timer.roundTime%60 else timer.roundTime%60}",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(0.2f),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "${timer.delay/60}:${if(timer.delay%60 < 10) "0" + timer.delay%60 else timer.delay%60}",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(0.2f),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "${timer.rounds}",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(0.2f),
            color = MaterialTheme.colorScheme.primary
        )
        
        Icon(
            painter = rememberVectorPainter(image = Icons.Filled.PlayArrow),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    globalTimer = timer
                    ServiceHelper.triggerForegroundService(
                        context = context,
                        action = Constants.ACTION_SERVICE_START
                    )
                    navHostController.popBackStack()
                }
        )
    }

}