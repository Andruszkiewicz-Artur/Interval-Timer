package com.example.intervaltimer.future_intervalTimer.present.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
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
import kotlinx.coroutines.flow.collectLatest
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
                overallTimer()
            }
            is HomeEvent.setPrepareTime -> {
                _state.value = state.value.copy(
                    timer = state.value.timer.copy(
                        startTime = event.time.toInt()
                    )
                )
                overallTimer()
            }
            is HomeEvent.setRoundTime -> {
                _state.value = state.value.copy(
                    timer = state.value.timer.copy(
                        roundTime = event.time.toInt()
                    )
                )
                overallTimer()
            }
            is HomeEvent.setRounds -> {
                _state.value = state.value.copy(
                   timer = state.value.timer.copy(
                       rounds = event.time.toInt()
                   )
                )
                overallTimer()
            }
            is HomeEvent.ChangeTimerValue -> {
                var timerValue = _state.value.timer

                when (event.timerValue) {
                    ChangeTimerValueEnum.Add -> {
                        when(event.timerType) {
                            TimerOptionEnum.Prepare -> {
                                timerValue = timerValue.copy(startTime = changeTimerValue(timerValue.startTime, ChangeTimerValueEnum.Add))
                            }
                            TimerOptionEnum.RoundTime -> {
                                timerValue = timerValue.copy(roundTime = changeTimerValue(timerValue.roundTime, ChangeTimerValueEnum.Add))
                            }
                            TimerOptionEnum.Break -> {
                                timerValue = timerValue.copy(delay = changeTimerValue(timerValue.delay, ChangeTimerValueEnum.Add))
                            }
                            TimerOptionEnum.Rounds -> {
                                timerValue = timerValue.copy(rounds = timerValue.rounds + 1)
                            }
                        }
                    }
                    ChangeTimerValueEnum.Subtract -> {
                        when(event.timerType) {
                            TimerOptionEnum.Prepare -> {
                                if (timerValue.startTime > 0) {
                                    timerValue = timerValue.copy(startTime = changeTimerValue(timerValue.startTime, ChangeTimerValueEnum.Subtract))
                                }
                            }
                            TimerOptionEnum.RoundTime -> {
                                if (timerValue.roundTime > 5) {
                                    timerValue = timerValue.copy(roundTime = changeTimerValue(timerValue.roundTime, ChangeTimerValueEnum.Subtract))
                                }
                            }
                            TimerOptionEnum.Break -> {
                                if (timerValue.delay > 5) {
                                    timerValue = timerValue.copy(delay = changeTimerValue(timerValue.delay, ChangeTimerValueEnum.Subtract))
                                }
                            }
                            TimerOptionEnum.Rounds -> {
                                if (timerValue.rounds > 1) {
                                    timerValue = timerValue.copy(rounds = timerValue.rounds - 1)
                                }
                            }
                        }
                    }
                }

                _state.value = state.value.copy(timer = timerValue)
                overallTimer()
            }
        }
        isExistTimer()
    }

    private fun getAllOwnIntervalTimes() {
        viewModelScope.launch {
            ownIntervalTimeUseCases.getAllOwnIntervalTimesUseCase.invoke().collectLatest { ownTimers ->
                _state.value = _state.value.copy(
                    ownIntervalTimes = ownTimers.toMutableList()
                )

                isExistTimer()
            }
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

    private fun overallTimer() {
        //Add start round
        var overallTime = _state.value.timer.startTime
        //Add resul off all rounds
        overallTime += _state.value.timer.roundTime * _state.value.timer.rounds
        //Add result of all delays
        overallTime += _state.value.timer.delay * (_state.value.timer.rounds - 1)

        _state.value = state.value.copy(
            overallTime = overallTime
        )
    }

    private fun changeTimerValue(currentValue: Int, timerValue: ChangeTimerValueEnum): Int {
        return when (timerValue) {
            ChangeTimerValueEnum.Add -> {
                if (currentValue < 60) {
                    currentValue + 5
                } else if(currentValue < 300) {
                    currentValue + 10
                } else {
                    currentValue + 30
                }
            }
            ChangeTimerValueEnum.Subtract -> {
                if (currentValue <= 60) {
                    currentValue - 5
                } else if(currentValue <= 300) {
                    currentValue - 10
                } else {
                    currentValue - 30
                }
            }
        }
    }
}