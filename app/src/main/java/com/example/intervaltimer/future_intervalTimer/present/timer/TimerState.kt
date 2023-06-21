package com.example.intervaltimer.future_intervalTimer.present.timer

import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel

data class TimerState(
    val currentTime: Int = 0,
    val currentRound: Int = 0,
    val isStop: Boolean = false,
    val currentStatus: TimerStateEnum = TimerStateEnum.Preparing
)

enum class TimerStateEnum{
    Preparing, Round, Break
}