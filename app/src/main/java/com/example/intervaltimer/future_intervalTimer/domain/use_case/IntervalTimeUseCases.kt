package com.example.intervaltimer.future_intervalTimer.domain.use_case

data class IntervalTimeUseCases(
    val getAllIntervalTimesUseCase: GetAllIntervalTimesUseCase,
    val getIntervalTimeUseCase: GetIntervalTimeUseCase,
    val insertIntervalTimeUseCase: InsertIntervalTimeUseCase
)
