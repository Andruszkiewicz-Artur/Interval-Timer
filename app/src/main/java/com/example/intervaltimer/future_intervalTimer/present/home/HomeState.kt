package com.example.intervaltimer.future_intervalTimer.present.home

import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel

data class HomeState(
    var ownIntervalTimes: MutableList<OwnIntervalTime> = mutableListOf(),
    val timer: TimerModel = TimerModel(
        startTime = 15,
        roundTime = 180,
        delay = 60,
        rounds = 5
    ),
    val timerExist: Boolean? = null
)
