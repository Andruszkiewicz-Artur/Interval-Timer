package com.example.intervaltimer.future_intervalTimer.present.ownIntervalTimes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.use_case.ownIntervalTime.OwnIntervalTimeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        getAllOwnIntervalTimes()
    }

    fun deleteOwnIntervalTime(ownIntervalTime: OwnIntervalTime) {
        viewModelScope.launch {
            ownIntervalTimeUseCases.deleteOwnIntervalTimeUseCase.invoke(ownIntervalTime)
            getAllOwnIntervalTimes()
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