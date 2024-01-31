package com.example.intervaltimer.future_intervalTimer.present.home.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CurrentChoosePresentation(
    isTimer: Boolean = true,
    time: Int,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                onClick()
            }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Thin
        )
        Text(
            text = if (isTimer) {
                "${time/60}m ${if((time%60) < 10) "0" + time%60 else time%60}s"
            } else {
                "$time"
            },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}