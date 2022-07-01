package com.example.intervaltimer.View

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.intervaltimer.ui.theme.Red

@Composable
fun TextField(
    title: String,
    text: Int,
    value: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = text.toString(),
            onValueChange = {
                value(if(it.toIntOrNull() == null) 0 else it.toInt())
            },
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Red,
                    shape = RoundedCornerShape(50)
                ),
            textStyle = TextStyle(
                textAlign = TextAlign.Center
            )
        )
    }
}