package com.example.intervaltimer.future_intervalTimer.present.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.use_case.intervalTime.IntervalTimeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val intervalTimeUseCases: IntervalTimeUseCases
): ViewModel() {

    fun insertIntervalTimer(intervalTime: IntervalTime) {
        viewModelScope.launch {
            intervalTimeUseCases.insertIntervalTimeUseCase(intervalTime)
        }
    }

}