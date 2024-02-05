package com.example.intervaltimer.future_intervalTimer.present.home.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intervaltimer.core.extension.toTime

@Composable
fun CurrentChoosePresentation(
    isTimer: Boolean = true,
    time: Int,
    text: String,
    onClick: () -> Unit,
    onClickMinus: () -> Unit,
    onClickPlus: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(vertical = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Thin
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(onClick = {
                    onClickMinus()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = null,
                        modifier = Modifier
                            .size(MaterialTheme.typography.headlineMedium.fontSize.value.dp)
                    )
                }

                Text(
                    text = if (isTimer) {
                        time.toTime()
                    } else {
                        "$time"
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .clickable {
                            onClick()
                        }
                )

                IconButton(onClick = {
                    onClickPlus()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .size(MaterialTheme.typography.headlineMedium.fontSize.value.dp)
                    )
                }
            }
        }
    }
}