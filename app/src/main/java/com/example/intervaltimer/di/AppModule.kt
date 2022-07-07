package com.example.intervaltimer.di

import android.app.Application
import androidx.room.Room
import com.example.intervaltimer.future_intervalTimer.data.data_source.IntervalTimeDatabase
import com.example.intervaltimer.future_intervalTimer.data.repository.IntervalTimeRepositoryImpl
import com.example.intervaltimer.future_intervalTimer.domain.repository.IntervalTimeRepository
import com.example.intervaltimer.future_intervalTimer.domain.use_case.GetAllIntervalTimesUseCase
import com.example.intervaltimer.future_intervalTimer.domain.use_case.GetIntervalTimeUseCase
import com.example.intervaltimer.future_intervalTimer.domain.use_case.InsertIntervalTimeUseCase
import com.example.intervaltimer.future_intervalTimer.domain.use_case.IntervalTimeUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideIntervalTimeDatabase(app: Application): IntervalTimeDatabase {
        return Room.databaseBuilder(
            app,
            IntervalTimeDatabase::class.java,
            IntervalTimeDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideIntervalTimeRepository(db: IntervalTimeDatabase): IntervalTimeRepository {
        return IntervalTimeRepositoryImpl(db.intervalTimeDao)
    }

    @Provides
    @Singleton
    fun provideIntervalTimeUseCases(repository: IntervalTimeRepository): IntervalTimeUseCases {
        return IntervalTimeUseCases(
            getAllIntervalTimesUseCase = GetAllIntervalTimesUseCase(repository),
            getIntervalTimeUseCase = GetIntervalTimeUseCase(repository),
            insertIntervalTimeUseCase = InsertIntervalTimeUseCase(repository)
        )
    }

}