package com.example.intervaltimer.future_intervalTimer.present.home

import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel

sealed class HomeEvent {
    object InsertOwnIntervalTime: HomeEvent()
    data class setPrepareTime(val time: Long): HomeEvent()
    data class setRoundTime(val time: Long): HomeEvent()
    data class setBreakTime(val time: Long): HomeEvent()
    data class setRounds(val time: Long): HomeEvent()
}
