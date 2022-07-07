package com.example.intervaltimer.future_intervalTimer.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTime

@Database(
    entities = [IntervalTime::class],
    version = 1
)
abstract class IntervalTimeDatabase: RoomDatabase() {

    abstract val intervalTimeDao: IntervalTimeDao

    companion object {
        const val DATABASE_NAME = "intervalTime_database"
    }

}