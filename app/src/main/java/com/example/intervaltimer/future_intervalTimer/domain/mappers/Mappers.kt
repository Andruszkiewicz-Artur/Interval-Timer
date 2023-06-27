package com.example.intervaltimer.future_intervalTimer.domain.mappers

import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel

fun OwnIntervalTime.toTimerModel(): TimerModel {
    return TimerModel(
        prepareTime,
        roundTime,
        breakTime,
        roundsCount
    )
}