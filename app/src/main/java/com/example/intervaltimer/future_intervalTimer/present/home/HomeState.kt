package com.example.intervaltimer.future_intervalTimer.present.home

import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime

data class HomeState(
    var ownIntervalTimes: MutableList<OwnIntervalTime> = mutableListOf(),
    val timeToPrepare: Int = 15,
    val roundTime: Int = 180,
    val breakTime: Int = 60,
    val rounds: Int = 5,
    val timerExist: Boolean = false
)
