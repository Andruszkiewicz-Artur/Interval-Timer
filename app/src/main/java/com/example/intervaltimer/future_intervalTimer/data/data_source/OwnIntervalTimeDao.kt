package com.example.intervaltimer.future_intervalTimer.data.data_source

import androidx.room.*
import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnIntervalTimeDao {

    @Query("SELECT * FROM ownIntervalTime")
    fun getAllOwnIntervalTimes(): Flow<List<OwnIntervalTime>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOwnIntervalTime(ownIntervalTime: OwnIntervalTime)

    @Delete
    suspend fun deleteOwnIntervalTIme(ownIntervalTime: OwnIntervalTime)

}