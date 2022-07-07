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
    var date: Date
)
