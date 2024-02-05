package com.example.intervaltimer.future_intervalTimer.present.util.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.intervaltimer.R
import com.example.intervaltimer.core.extension.toTime
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel

@Composable
fun ExistingTimerPresentation(
    timer: TimerModel,
    isPossibleToDelete: Boolean = true,
    onClickStart: () -> Unit,
    onClickRemove: () -> Unit = {  }
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Unknown Name",
                    style = MaterialTheme.typography.headlineMedium
                )
                
                if (isPossibleToDelete) {
                    Icon(
                        imageVector = Icons.Filled.DeleteOutline,
                        contentDescription = stringResource(id = R.string.DeleteIntervalTime),
                        modifier = Modifier
                            .padding(0.dp)
                            .size(30.dp)
                            .clickable {
                                onClickRemove()
                            }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    onClickStart()
                }) {
                    Text(
                        text = stringResource(id = R.string.Start)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${timer.rounds} "
                    )

                    Text(
                        text = stringResource(id = if (timer.rounds == 1) R.string.Set else R.string.Sets),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = timer.roundTime.toTime()
                )
            }
        }
    }

}