package com.example.intervaltimer.future_intervalTimer.data.repository

import com.example.intervaltimer.future_intervalTimer.data.data_source.OwnIntervalTimeDao
import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.repository.OwnIntervalTimeRepository
import kotlinx.coroutines.flow.Flow

class OwnIntervalTimeRepositoryImpl(
    private val dao: OwnIntervalTimeDao
): OwnIntervalTimeRepository {

    override fun getAllOwnIntervalTimes(): Flow<List<OwnIntervalTime>> = dao.getAllOwnIntervalTimes()

    override suspend fun insertOwnIntervalTime(ownIntervalTime: OwnIntervalTime) = dao.insertOwnIntervalTime(ownIntervalTime)

    override suspend fun deleteOwnIntervalTime(ownIntervalTime: OwnIntervalTime) = dao.deleteOwnIntervalTIme(ownIntervalTime)


}