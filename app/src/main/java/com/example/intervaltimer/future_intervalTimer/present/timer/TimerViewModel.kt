package com.example.intervaltimer.future_intervalTimer.present.timer

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.intervaltimer.R
import com.example.intervaltimer.core.constants.Constants.ACTION_SERVICE_CANCEL
import com.example.intervaltimer.core.constants.Constants.ACTION_SERVICE_START
import com.example.intervaltimer.core.constants.Constants.ACTION_SERVICE_STOP
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel
import com.example.intervaltimer.future_intervalTimer.domain.service.ServiceHelper
import com.example.intervaltimer.future_intervalTimer.domain.use_case.intervalTime.IntervalTimeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalAnimationApi::class)
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val intervalTimeUseCases: IntervalTimeUseCases,
    private val application: Application
): ViewModel() {

    private val _shareFlow = MutableSharedFlow<TimerUiEvent>()
    val shareFlow = _shareFlow

    fun onEvent(event: TimerEvent) {
        when (event) {
            TimerEvent.Start -> {
                ServiceHelper.triggerForegroundService(
                    context = application,
                    action = ACTION_SERVICE_START
                )
            }
            TimerEvent.Stop -> {
                ServiceHelper.triggerForegroundService(
                    context = application,
                    action = ACTION_SERVICE_STOP
                )
            }
            TimerEvent.Cancel -> {
                ServiceHelper.triggerForegroundService(
                    context = application, action = ACTION_SERVICE_CANCEL
                )
            }
        }
    }

    private fun playAudio(context: Context, id: Int) {

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
}