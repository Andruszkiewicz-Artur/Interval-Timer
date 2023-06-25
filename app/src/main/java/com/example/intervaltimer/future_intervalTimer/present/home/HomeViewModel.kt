package com.example.intervaltimer.future_intervalTimer.present.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intervaltimer.R
import com.example.intervaltimer.core.Event.UiEvent
import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel
import com.example.intervaltimer.future_intervalTimer.domain.use_case.ownIntervalTime.OwnIntervalTimeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ownIntervalTimeUseCases: OwnIntervalTimeUseCases
): ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state


    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getAllOwnIntervalTimes()
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.InsertOwnIntervalTime -> {
                viewModelScope.launch {
                    if(_state.value.timerExist == false) {
                        ownIntervalTimeUseCases.insertOwnIntervalTimeUseCase.invoke(_state.value.timer.toOwnIntervalTimer())
                        _state.value.ownIntervalTimes.add(_state.value.timer.toOwnIntervalTimer())
                        _eventFlow.emit(UiEvent.ShowToast(R.string.YouAddNewIntervalTime))
                    } else if (_state.value.timerExist == null) {
                        _eventFlow.emit(UiEvent.ShowToast(R.string.ProblemWithAddingNewTimer))
                    }else {
                        _eventFlow.emit(UiEvent.ShowToast(R.string.IntervalTimeLikeThatExistAtNow))
                    }
                }
            }

            is HomeEvent.setBreakTime -> {
                _state.value = state.value.copy(
                    timer = state.value.timer.copy(
                        delay = event.time.toInt()
                    )
                )
            }
            is HomeEvent.setPrepareTime -> {
                _state.value = state.value.copy(
                    timer = state.value.timer.copy(
                        startTime = event.time.toInt()
                    )
                )
            }
            is HomeEvent.setRoundTime -> {
                _state.value = state.value.copy(
                    timer = state.value.timer.copy(
                        roundTime = event.time.toInt()
                    )
                )
            }
            is HomeEvent.setRounds -> {
                _state.value = state.value.copy(
                   timer = state.value.timer.copy(
                       rounds = event.time.toInt()
                   )
                )
            }
        }
        isExistTimer()
    }

    private fun getAllOwnIntervalTimes() {
        viewModelScope.launch {
            ownIntervalTimeUseCases.getAllOwnIntervalTimesUseCase.invoke().onEach { ownIntervalTimes ->
                _state.value = _state.value.copy(
                    ownIntervalTimes = ownIntervalTimes.toMutableList()
                )
            }.launchIn(viewModelScope)

            delay(500)
            isExistTimer()
        }
    }

    private fun isExistTimer() {
        viewModelScope.launch {
            delay(300)

            val isExist = _state.value.ownIntervalTimes.filter { ownTimer ->
                ownTimer.toTimer() == _state.value.timer
            }.isNotEmpty()

            _state.value = state.value.copy(
                timerExist = isExist
            )
        }
    }
}