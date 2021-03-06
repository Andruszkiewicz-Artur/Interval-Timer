package com.example.intervaltimer.future_intervalTimer.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "intervalTime")
data class IntervalTime(
    @PrimaryKey var id: Int? = null,
    var prepareTime: Int,
    var roundTime: Int,
    var breakTime: Int,
    var roundsCount: Int,
    var date: String
) {
    fun toTimer(): TimerModel {
        return TimerModel(
            startTime = this.prepareTime,
            roundTime = this.roundTime,
            delay = this.breakTime,
            rounds = this.roundsCount
        )
    }
}
