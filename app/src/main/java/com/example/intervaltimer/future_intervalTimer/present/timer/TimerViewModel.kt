package com.example.intervaltimer.future_intervalTimer.present.timer

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intervaltimer.R
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel
import com.example.intervaltimer.future_intervalTimer.domain.use_case.intervalTime.IntervalTimeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val intervalTimeUseCases: IntervalTimeUseCases,
    private val context: Context
): ViewModel() {

    private val _shareFlow = MutableSharedFlow<TimerUiEvent>()
    val shareFlow = _shareFlow

    private var _timer = TimerModel(0,0,0,0)

    private val _state = mutableStateOf(TimerState())
    val state: State<TimerState> = _state

    fun onEvent(event: TimerEvent) {
        when (event) {
            TimerEvent.Start -> {
                _state.value = state.value.copy(
                    isStop = false
                )
                Log.d("Check start", "${_state.value.isStop}")
                countingTime()
            }
            TimerEvent.Stop -> {
                _state.value = state.value.copy(
                    isStop = true
                )
                Log.d("Check stop", "${_state.value.isStop}")
            }
        }
    }

    private fun countingTime() {
        viewModelScope.launch {
            while (!_state.value.isStop) {
                delay(100)
                _state.value = state.value.copy(
                    currentTime = _state.value.currentTime - 1
                )

                if (_state.value.currentTime == 0) {
                    when (_state.value.currentStatus) {
                        TimerStateEnum.Preparing -> {
                            _state.value = state.value.copy(
                                currentTime = _timer.roundTime * 10,
                                currentRound = 1,
                                currentStatus = TimerStateEnum.Round
                            )
                        }
                        TimerStateEnum.Round -> {
                            if (_state.value.currentRound != _timer.rounds) {
                                _state.value = state.value.copy(
                                    currentTime = _timer.delay * 10,
                                    currentRound = _state.value.currentRound + 1,
                                    currentStatus = TimerStateEnum.Break
                                )
                                playAudio(context, R.raw.gong)
                            } else {
                                shareFlow.emit(TimerUiEvent.Finish)
                                playAudio(context, R.raw.bell_finish)
                            }
                        }
                        TimerStateEnum.Break -> {
                            _state.value = state.value.copy(
                                currentTime = _timer.roundTime * 10,
                                currentStatus = TimerStateEnum.Round
                            )
                            playAudio(context, R.raw.bell_soon)
                        }
                    }
                }
            }
        }
    }

    fun playAudio(context: Context, id: Int) {

        var mMediaPlayer: MediaPlayer

        try {

            mMediaPlayer = MediaPlayer()
            mMediaPlayer = MediaPlayer.create(context, id)

            mMediaPlayer.setOnPreparedListener {
                mMediaPlayer.start()
            }

            mMediaPlayer.setOnCompletionListener {
                mMediaPlayer.release()
            }

        }
        catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setupTimer(timerModel: TimerModel) {
        _timer = timerModel

        _state.value = state.value.copy(
            currentTime = _timer.startTime * 10,
            currentRound = 0,
            isStop = false,
            currentStatus = TimerStateEnum.Preparing
        )

        Log.d("Check state", "${_state.value.isStop}")

        countingTime()
    }
}