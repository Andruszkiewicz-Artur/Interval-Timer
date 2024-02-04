package com.example.intervaltimer.core.extension

fun Int.toTime(): String {
    val minutes = this/60
    val seconds = if (this%60 > 9) this%60 else "0" + this%60

    return "$minutes:$seconds"
}