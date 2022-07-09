package com.example.intervaltimer.future_intervalTimer.present.home.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.intervaltimer.View.TextField

@Composable
fun timerOption(
    title: String,
    firstField: Pair<String, Int>,
    secondField: Pair<String, Int>? = null,
    value: (Int, Int?) -> Unit
) {
    var first by remember { mutableStateOf(firstField.second) }
    var secound by remember { mutableStateOf(secondField?.second) }

    Text(
        text = title
    )
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            title = firstField.first,
            text = firstField.second,
            value = {
                first = it
                value(first, secound)
            }
        )
        if (secondField != null) {
            Spacer(modifier = Modifier.width(10.dp))
            TextField(
                title = secondField.first,
                text = secondField.second,
                value = {
                    secound = it
                    value(first, secound)
                }
            )
        }
    }

}