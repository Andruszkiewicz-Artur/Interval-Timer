package com.example.intervaltimer.future_intervalTimer.domain.repository

import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import kotlinx.coroutines.flow.Flow

interface OwnIntervalTimeRepository {

    fun getAllOwnIntervalTimes(): Flow<List<OwnIntervalTime>>

    suspend fun insertOwnIntervalTime(ownIntervalTime: OwnIntervalTime)

    suspend fun deleteOwnIntervalTime(ownIntervalTime: OwnIntervalTime)

}