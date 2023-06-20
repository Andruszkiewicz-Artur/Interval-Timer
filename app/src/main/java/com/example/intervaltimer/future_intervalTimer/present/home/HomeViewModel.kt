package com.example.intervaltimer.future_intervalTimer.present.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                    delay(500L)
                    val timerModel = TimerModel(
                        startTime = _state.value.timeToPrepare,
                        roundTime = _state.value.roundTime,
                        delay = _state.value.breakTime,
                        rounds = _state.value.rounds
                    )

                    val result = _state.value.ownIntervalTimes.filter { ownTimer ->
                        ownTimer.toTimer() == timerModel
                    }.isEmpty()

                    if(result) {
                        ownIntervalTimeUseCases.insertOwnIntervalTimeUseCase.invoke(timerModel.toOwnIntervalTimer())
                        _state.value.ownIntervalTimes.add(timerModel.toOwnIntervalTimer())
                        _eventFlow.emit(UiEvent.ShowToast("You Add new Interval Time!"))
                    } else {
                        _eventFlow.emit(UiEvent.ShowToast("Interval Time like that exist at now!"))
                    }
                }
            }
        }
    }

    private fun getAllOwnIntervalTimes() {
        ownIntervalTimeUseCases.getAllOwnIntervalTimesUseCase.invoke().onEach { ownIntervalTimes ->
            _state.value = _state.value.copy(
                ownIntervalTimes = ownIntervalTimes.toMutableList()
            )
        }.launchIn(viewModelScope)
    }
}