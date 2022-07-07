package com.example.intervaltimer.future_intervalTimer.domain.use_case

import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.repository.IntervalTimeRepository

class GetIntervalTimeUseCase(
    private val repository: IntervalTimeRepository
) {

    suspend operator fun invoke(id: Int): IntervalTime? = repository.getIntervalTime(id)

}