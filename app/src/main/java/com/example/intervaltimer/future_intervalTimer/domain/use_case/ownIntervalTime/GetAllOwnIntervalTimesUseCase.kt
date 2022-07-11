package com.example.intervaltimer.future_intervalTimer.domain.use_case.ownIntervalTime

import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.repository.OwnIntervalTimeRepository
import kotlinx.coroutines.flow.Flow

class GetAllOwnIntervalTimesUseCase(
    private val repository: OwnIntervalTimeRepository
) {

    operator fun invoke(): Flow<List<OwnIntervalTime>> {
        return repository.getAllOwnIntervalTimes()
    }

}