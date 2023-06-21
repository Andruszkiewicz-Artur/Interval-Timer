package com.example.intervaltimer.future_intervalTimer.present.timer.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ButtonTimerPresentation(
    icon: ImageVector,
    isColor: Boolean = false,
    onClick: () -> Unit
) {
    val backGroundColor = MaterialTheme.colors.background
    val tint = MaterialTheme.colors.onSurface

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .shadow(
                shape = CircleShape,
                elevation = 15.dp
            )
            .size(80.dp)
            .background(
                color = backGroundColor,
                shape = CircleShape
            )
            .clickable {
                onClick()
            }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .fillMaxSize(0.7f)
        )
    }
}
