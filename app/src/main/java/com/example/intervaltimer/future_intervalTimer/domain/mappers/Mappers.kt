package com.example.intervaltimer.future_intervalTimer.domain.mappers

import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel
import java.util.Calendar

fun OwnIntervalTime.toTimerModel(): TimerModel {
    return TimerModel(
        prepareTime,
        roundTime,
        breakTime,
        roundsCount
    )
}

fun TimerModel.toIntervalTime(): IntervalTime {
    return IntervalTime(
        prepareTime = startTime,
        roundTime = roundTime,
        breakTime = delay,
        roundsCount = rounds,
        date = Calendar.getInstance().timeInMillis
    )
}