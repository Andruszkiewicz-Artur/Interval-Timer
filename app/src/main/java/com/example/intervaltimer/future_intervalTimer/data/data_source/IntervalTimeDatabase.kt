package com.example.intervaltimer.future_intervalTimer.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime

@Database(
    entities = [IntervalTime::class, OwnIntervalTime::class],
    version = 1
)
abstract class IntervalTimeDatabase: RoomDatabase() {

    abstract val intervalTimeDao: IntervalTimeDao
    abstract val ownIntervalTimeDao: OwnIntervalTimeDao

    companion object {
        const val DATABASE_NAME = "intervalTime_database"
    }

}