package com.example.intervaltimer.di

import android.app.Application
import androidx.room.Room
import com.example.intervaltimer.future_intervalTimer.data.data_source.IntervalTimeDatabase
import com.example.intervaltimer.future_intervalTimer.data.repository.IntervalTimeRepositoryImpl
import com.example.intervaltimer.future_intervalTimer.data.repository.OwnIntervalTimeRepositoryImpl
import com.example.intervaltimer.future_intervalTimer.domain.repository.IntervalTimeRepository
import com.example.intervaltimer.future_intervalTimer.domain.repository.OwnIntervalTimeRepository
import com.example.intervaltimer.future_intervalTimer.domain.use_case.intervalTime.GetAllIntervalTimesUseCase
import com.example.intervaltimer.future_intervalTimer.domain.use_case.intervalTime.GetIntervalTimeUseCase
import com.example.intervaltimer.future_intervalTimer.domain.use_case.intervalTime.InsertIntervalTimeUseCase
import com.example.intervaltimer.future_intervalTimer.domain.use_case.intervalTime.IntervalTimeUseCases
import com.example.intervaltimer.future_intervalTimer.domain.use_case.ownIntervalTime.DeleteOwnIntervalTimeUseCase
import com.example.intervaltimer.future_intervalTimer.domain.use_case.ownIntervalTime.GetAllOwnIntervalTimesUseCase
import com.example.intervaltimer.future_intervalTimer.domain.use_case.ownIntervalTime.InsertOwnIntervalTimeUseCase
import com.example.intervaltimer.future_intervalTimer.domain.use_case.ownIntervalTime.OwnIntervalTimeUseCases
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
    fun provideOwnIntervalTimeRepository(db: IntervalTimeDatabase): OwnIntervalTimeRepository {
        return OwnIntervalTimeRepositoryImpl(db.ownIntervalTimeDao)
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

    @Provides
    @Singleton
    fun provideOwnIntervalTimeUseCases(repository: OwnIntervalTimeRepository): OwnIntervalTimeUseCases {
        return OwnIntervalTimeUseCases(
            getAllOwnIntervalTimesUseCase = GetAllOwnIntervalTimesUseCase(repository),
            insertOwnIntervalTimeUseCase = InsertOwnIntervalTimeUseCase(repository),
            deleteOwnIntervalTimeUseCase = DeleteOwnIntervalTimeUseCase(repository)
        )
    }

}