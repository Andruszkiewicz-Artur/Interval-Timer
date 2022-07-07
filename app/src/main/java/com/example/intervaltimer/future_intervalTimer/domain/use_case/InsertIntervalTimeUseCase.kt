package com.example.intervaltimer.future_intervalTimer.domain.use_case

import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.repository.IntervalTimeRepository

class InsertIntervalTimeUseCase(
    private val repository: IntervalTimeRepository
) {

    suspend operator fun invoke(intervalTime: IntervalTime) = repository.insertIntervalTime(intervalTime)

}