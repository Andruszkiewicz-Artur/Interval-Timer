package com.example.intervaltimer.future_intervalTimer.domain.model

import java.util.*

data class TimerModel(
    val name: String,
    val startTime: Int,
    val roundTime: Int,
    val delay: Int,
    val rounds: Int
) {
    fun toIntervalTimer(): IntervalTime {
        return IntervalTime(
            prepareTime = this.startTime,
            roundTime = this.roundTime,
            breakTime = this.delay,
            roundsCount = this.rounds,
            date = System.currentTimeMillis(),
            name = this.name
        )
    }

    fun toOwnIntervalTimer(): OwnIntervalTime {
        return OwnIntervalTime(
            prepareTime = this.startTime,
            breakTime = this.delay,
            roundTime = this.roundTime,
            roundsCount = this.rounds,
            name = this.name
        )
    }
}
