package com.example.intervaltimer.future_intervalTimer.present.home.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeButton(
    image: ImageVector,
    text: String,
    imageColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
    textColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable {
                onClick()
            }
    ) {
        Icon(
            imageVector = image,
            contentDescription = null,
            tint = imageColor,
            modifier = Modifier
                .size(50.dp)
        )
        Text(
            text = text,
            color = textColor,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light
        )
    }
}