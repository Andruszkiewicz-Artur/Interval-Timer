package com.example.intervaltimer.future_intervalTimer.present.history

import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTime

data class IntervalTimeState(
    val intervalTimes: List<IntervalTime> = emptyList()
)
