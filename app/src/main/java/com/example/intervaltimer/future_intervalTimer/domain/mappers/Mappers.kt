package com.example.intervaltimer.future_intervalTimer.domain.mappers

import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel
import java.util.Calendar

fun OwnIntervalTime.toTimerModel(): TimerModel {
    return TimerModel(
        name,
        prepareTime,
        roundTime,
        breakTime,
        roundsCount
    )
}