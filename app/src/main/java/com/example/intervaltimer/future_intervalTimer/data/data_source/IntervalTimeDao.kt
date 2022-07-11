package com.example.intervaltimer.future_intervalTimer.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTime
import kotlinx.coroutines.flow.Flow

@Dao
interface IntervalTimeDao {

    @Query("SELECT * FROM IntervalTime")
    fun getAllIntervalTimes(): Flow<List<IntervalTime>>

    @Query("SELECT * FROM IntervalTime WHERE id = :id ORDER BY date ASC")
    suspend fun getIntervalTime(id: Int): IntervalTime?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntervalTime(intervalTime: IntervalTime)
    
}