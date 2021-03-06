package com.example.intervaltimer.future_intervalTimer.present.ownIntervalTimes.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import com.example.intervaltimer.future_intervalTimer.present.ownIntervalTimes.ownIntervalTimesViewModel
import com.example.intervaltimer.future_intervalTimer.present.util.screen.Screen
import com.example.intervaltimer.ui.theme.Blue50

@Composable
fun OwnIntervalTimesItem(
    navHostController: NavHostController,
    ownIntervalTime: OwnIntervalTime,
    viewModel: ownIntervalTimesViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(20.dp)
            )
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {

        Text(
            text = "${ownIntervalTime.prepareTime/60}:${if(ownIntervalTime.prepareTime%60 < 10) "0" + ownIntervalTime.prepareTime%60 else ownIntervalTime.prepareTime%60}",
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "${ownIntervalTime.roundTime/60}:${if(ownIntervalTime.roundTime%60 < 10) "0" + ownIntervalTime.roundTime%60 else ownIntervalTime.roundTime%60}",
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "${ownIntervalTime.breakTime/60}:${if(ownIntervalTime.breakTime%60 < 10) "0" + ownIntervalTime.breakTime%60 else ownIntervalTime.breakTime%60}",
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "${ownIntervalTime.roundsCount}",
            color = MaterialTheme.colorScheme.primary
        )

        Icon(
            painter = rememberVectorPainter(image = Icons.Filled.Delete),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    viewModel.onEvent(OwnIntervalTimeEvent.DeleteOwnIntervalTime(ownIntervalTime))
                }
        )

        Icon(
            painter = rememberVectorPainter(image = Icons.Filled.PlayArrow),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    navHostController.navigate(Screen.Timer.sendData(ownIntervalTime.toTimer()))
                }
        )
    }
}