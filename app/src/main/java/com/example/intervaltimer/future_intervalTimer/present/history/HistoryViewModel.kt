package com.example.intervaltimer.future_intervalTimer.present.history

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intervaltimer.future_intervalTimer.domain.use_case.IntervalTimeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val intervalTimeUseCases: IntervalTimeUseCases
): ViewModel() {

    private val _state = mutableStateOf(IntervalTimeState())
    val state: State<IntervalTimeState> = _state

    init {
        getAllIntervalTimer()
    }

    private fun getAllIntervalTimer() {
        intervalTimeUseCases.getAllIntervalTimesUseCase.invoke().onEach { intervalTimes ->
            _state.value = state.value.copy(
                intervalTimes = intervalTimes
            )
        }.launchIn(viewModelScope)
    }

}