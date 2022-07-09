package com.example.intervaltimer.future_intervalTimer.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class IntervalTime(
    @PrimaryKey var id: Int? = null,
    var prepareTime: Int,
    var roundTime: Int,
    var breakTime: Int,
    var roundsCount: Int,
    var date: String
) {
    companion object {
        fun toTimer(intervalTime: IntervalTime): TimerModel {
            return TimerModel(
                startTime = intervalTime.prepareTime,
                roundTime = intervalTime.roundTime,
                delay = intervalTime.breakTime,
                rounds = intervalTime.roundsCount
            )
        }
    }
}
