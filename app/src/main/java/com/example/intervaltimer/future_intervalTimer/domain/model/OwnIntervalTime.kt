package com.example.intervaltimer.future_intervalTimer.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ownIntervalTime")
data class OwnIntervalTime(
    @PrimaryKey var id: Int? = null,
    var prepareTime: Int,
    var roundTime: Int,
    var breakTime: Int,
    var roundsCount: Int
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
