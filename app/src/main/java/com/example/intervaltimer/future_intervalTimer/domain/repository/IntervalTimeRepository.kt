package com.example.intervaltimer.future_intervalTimer.domain.repository

import com.example.intervaltimer.future_intervalTimer.data.data_source.IntervalTimeDao
import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTime
import kotlinx.coroutines.flow.Flow

interface IntervalTimeRepository {

    fun getAllIntervalTimes(): Flow<List<IntervalTime>>

    suspend fun getIntervalTime(id: Int): IntervalTime?

    suspend fun insertIntervalTime(intervalTime: IntervalTime)

}