package com.example.intervaltimer.future_intervalTimer.present.home

import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime

data class HomeState(
    val ownIntervalTimes: List<OwnIntervalTime> = emptyList()
)
