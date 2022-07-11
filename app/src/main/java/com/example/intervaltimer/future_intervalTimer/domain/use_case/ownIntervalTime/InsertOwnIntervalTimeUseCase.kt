package com.example.intervaltimer.future_intervalTimer.domain.use_case.ownIntervalTime

import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.repository.OwnIntervalTimeRepository

class InsertOwnIntervalTimeUseCase(
    private val repository: OwnIntervalTimeRepository
) {

    suspend operator fun invoke(ownIntervalTime: OwnIntervalTime) {
        return repository.insertOwnIntervalTime(ownIntervalTime)
    }

}