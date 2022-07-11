package com.example.intervaltimer.future_intervalTimer.present.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel
import com.example.intervaltimer.future_intervalTimer.domain.use_case.ownIntervalTime.OwnIntervalTimeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ownIntervalTimeUseCases: OwnIntervalTimeUseCases
): ViewModel() {

    private val _state = mutableStateOf(HomeState())
    private val state: State<HomeState> = _state

    fun insertOwnIntervalTime(timerModel: TimerModel): Boolean {
        getAllOwnIntervalTimes()

        val result = _state.value.ownIntervalTimes.filter {
                        it.toTimer() == timerModel
                    }.isEmpty()

        if(result) {
            return false
        } else {
            viewModelScope.launch {
                ownIntervalTimeUseCases.insertOwnIntervalTimeUseCase.invoke(timerModel.toOwnIntervalTimer())
            }
            return true
        }
    }

    private fun getAllOwnIntervalTimes() {
        ownIntervalTimeUseCases.getAllOwnIntervalTimesUseCase.invoke().onEach { ownIntervalTimes ->
            _state.value = state.value.copy(
                ownIntervalTimes = ownIntervalTimes
            )
        }.launchIn(viewModelScope)
    }
}