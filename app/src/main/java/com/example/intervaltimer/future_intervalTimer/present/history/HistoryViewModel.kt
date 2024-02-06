package com.example.intervaltimer.future_intervalTimer.present.history

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intervaltimer.future_intervalTimer.domain.use_case.intervalTime.IntervalTimeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val intervalTimeUseCases: IntervalTimeUseCases
): ViewModel() {

    private val _state = MutableStateFlow(IntervalTimeState())
    val state = _state.asStateFlow()

    init {
        getAllIntervalTimer()
    }

    private fun getAllIntervalTimer() {
        intervalTimeUseCases.getAllIntervalTimesUseCase.invoke().onEach { intervalTimes ->
            _state.update { it.copy(
                intervalTimes = intervalTimes
            ) }
        }.launchIn(viewModelScope)
    }

}