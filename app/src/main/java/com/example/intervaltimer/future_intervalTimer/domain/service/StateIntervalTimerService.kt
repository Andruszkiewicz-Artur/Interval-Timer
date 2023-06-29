package com.example.intervaltimer.future_intervalTimer.domain.service

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTimeState
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerStateEnum
import kotlin.time.Duration

data class StateIntervalTimerService(
    val duration: Duration = Duration.ZERO,
    val status: TimerStateEnum = TimerStateEnum.Preparing,
    val round: Int = 0,
    val currentState: IntervalTimeState = IntervalTimeState.Idle
)
