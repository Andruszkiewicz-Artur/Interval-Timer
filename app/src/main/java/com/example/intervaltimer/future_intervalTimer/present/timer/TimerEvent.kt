package com.example.intervaltimer.future_intervalTimer.present.timer

sealed class TimerEvent {
    object Stop: TimerEvent()
    object Start: TimerEvent()
}
