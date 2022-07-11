package com.example.intervaltimer.future_intervalTimer.domain.use_case.intervalTime

import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.repository.IntervalTimeRepository
import kotlinx.coroutines.flow.Flow

class GetAllIntervalTimesUseCase(
    private val repository: IntervalTimeRepository
) {

    operator fun invoke(): Flow<List<IntervalTime>> {
        return repository.getAllIntervalTimes()
    }

}