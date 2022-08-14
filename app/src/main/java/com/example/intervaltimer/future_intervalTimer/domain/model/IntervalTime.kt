package com.example.intervaltimer.future_intervalTimer.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "intervalTime")
data class IntervalTime(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "prepareTime")
    var prepareTime: Int,

    @ColumnInfo(name = "roundTime")
    var roundTime: Int,

    @ColumnInfo(name = "breakTime")
    var breakTime: Int,

    @ColumnInfo(name = "roundsCount")
    var roundsCount: Int,

    @ColumnInfo(name = "date")
    var date: Long
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
