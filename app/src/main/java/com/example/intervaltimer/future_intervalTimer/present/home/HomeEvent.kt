package com.example.intervaltimer.future_intervalTimer.present.home

import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel

sealed class HomeEvent {
    object InsertOwnIntervalTime: HomeEvent()
    data class SetValue(val timerType: TimerOptionEnum, val time: Long): HomeEvent()
    data class ChangeTimerValue(val timerType: TimerOptionEnum, val timerValue: ChangeTimerValueEnum): HomeEvent()
}
