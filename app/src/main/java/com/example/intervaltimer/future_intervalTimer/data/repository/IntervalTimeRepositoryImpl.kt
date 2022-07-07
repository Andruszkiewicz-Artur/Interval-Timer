package com.example.intervaltimer.future_intervalTimer.data.repository

import com.example.intervaltimer.future_intervalTimer.data.data_source.IntervalTimeDao
import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.repository.IntervalTimeRepository
import kotlinx.coroutines.flow.Flow

class IntervalTimeRepositoryImpl(
    private val dao: IntervalTimeDao
): IntervalTimeRepository {

    override fun getAllIntervalTimes(): Flow<List<IntervalTime>> = dao.getAllIntervalTimes()

    override suspend fun getIntervalTime(id: Int): IntervalTime? = dao.getIntervalTime(id)

    override suspend fun insertIntervalTime(intervalTime: IntervalTime) = dao.insertIntervalTime(intervalTime)

}