package com.example.intervaltimer.future_intervalTimer.present.util.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel

@Composable
fun ItemPresentation(
    timer: TimerModel,
    isDelete: Boolean,
    onClickDelete: () -> Unit = { },
    onClickStart: () -> Unit
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

        if (isDelete) {
            Icon(
                painter = rememberVectorPainter(image = Icons.Filled.Delete),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        onClickDelete()
                    }
            )
        }

        Icon(
            painter = rememberVectorPainter(image = Icons.Filled.PlayArrow),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    onClickStart()
                }
        )
    }
}