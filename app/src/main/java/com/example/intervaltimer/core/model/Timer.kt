package com.example.intervaltimer.future_intervalTimer.domain.model

import java.util.*

data class TimerModel(
    val startTime: Int,
    val roundTime: Int,
    val delay: Int,
    val rounds: Int
) {
    companion object {
        fun toIntervalTimer(timerModel: TimerModel): IntervalTime {
            return IntervalTime(
                prepareTime = timerModel.startTime,
                roundTime = timerModel.roundTime,
                breakTime = timerModel.delay,
                roundsCount = timerModel.rounds,
                date = Calendar.getInstance().time.toString()
            )
        }
    }
}
