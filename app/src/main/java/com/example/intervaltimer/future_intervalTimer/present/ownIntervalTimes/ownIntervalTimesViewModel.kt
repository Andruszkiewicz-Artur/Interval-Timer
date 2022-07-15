package com.example.intervaltimer.future_intervalTimer.present.ownIntervalTimes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intervaltimer.core.Event.UiEvent
import com.example.intervaltimer.future_intervalTimer.domain.use_case.ownIntervalTime.OwnIntervalTimeUseCases
import com.example.intervaltimer.future_intervalTimer.present.ownIntervalTimes.compose.OwnIntervalTimeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ownIntervalTimesViewModel @Inject constructor(
    private var ownIntervalTimeUseCases: OwnIntervalTimeUseCases
): ViewModel() {

    private val _state = mutableStateOf(OwnIntervalTimeState())
    val state: State<OwnIntervalTimeState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow =_eventFlow.asSharedFlow()

    init {
        getAllOwnIntervalTimes()
    }

    fun onEvent(event: OwnIntervalTimeEvent) {
        when(event) {
            is OwnIntervalTimeEvent.DeleteOwnIntervalTime -> {
                viewModelScope.launch {
                    ownIntervalTimeUseCases.deleteOwnIntervalTimeUseCase.invoke(event.ownIntervalTime)
                    getAllOwnIntervalTimes()
                    _eventFlow.emit(UiEvent.ShowToast("Delete Interval Time"))
                }
            }
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