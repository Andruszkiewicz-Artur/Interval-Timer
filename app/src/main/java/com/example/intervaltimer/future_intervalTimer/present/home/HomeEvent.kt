package com.example.intervaltimer.future_intervalTimer.present.home

import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel

sealed class HomeEvent {
    data class InsertOwnIntervalTime(val timerModel: TimerModel): HomeEvent()
}
