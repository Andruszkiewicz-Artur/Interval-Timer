package com.example.intervaltimer.core.extensions

fun formatTime(seconds: String, minutes: String): String {
    return "$minutes:$seconds"
}

fun Int.pad(): String {
    return this.toString().padStart(2, '0')
}