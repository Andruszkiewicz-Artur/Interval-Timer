package com.example.intervaltimer.future_intervalTimer.domain.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.NotificationCompat
import com.example.intervaltimer.R
import com.example.intervaltimer.core.constants.Constants.ACTION_SERVICE_CANCEL
import com.example.intervaltimer.core.constants.Constants.ACTION_SERVICE_START
import com.example.intervaltimer.core.constants.Constants.ACTION_SERVICE_STOP
import com.example.intervaltimer.core.constants.Constants.INTERVAL_TIMER_STATE
import com.example.intervaltimer.core.constants.Constants.NOTIFICATION_CHANNEL_ID
import com.example.intervaltimer.core.constants.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.intervaltimer.core.constants.Constants.NOTIFICATION_ID
import com.example.intervaltimer.core.extensions.formatTime
import com.example.intervaltimer.core.extensions.pad
import com.example.intervaltimer.core.global.globalTimer
import com.example.intervaltimer.future_intervalTimer.domain.mappers.toIntervalTime
import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTimeState
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerStateEnum
import com.example.intervaltimer.future_intervalTimer.domain.use_case.intervalTime.IntervalTimeUseCases
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@ExperimentalAnimationApi
@AndroidEntryPoint
class IntervalTimeService : Service() {
    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder
    @Inject
    lateinit var context: Context
    @Inject
    lateinit var intervalTimeUseCases: IntervalTimeUseCases

    private val serviceScope = CoroutineScope(Dispatchers.Main)

    private val binder = IntervalTimerBinder()

    private lateinit var timer: Timer

    private var timerSetUp: TimerModel = globalTimer

    private val _state = mutableStateOf(StateIntervalTimerService())
    val state: State<StateIntervalTimerService> = _state

    override fun onBind(p0: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra(INTERVAL_TIMER_STATE)) {
            IntervalTimeState.Started.name -> {
                setStopButton()
                startForegroundService()
                startIntervalTimer { }
            }
            IntervalTimeState.Stopped.name -> {
                stopIntervalTimer()
                setResumeButton()
            }
            IntervalTimeState.Canceled.name -> {
                stopIntervalTimer()
                cancelIntervalTimer()
                stopForegroundService()
            }
        }
        intent?.action.let {
            when (it) {
                ACTION_SERVICE_START -> {
                    if(_state.value.currentState == IntervalTimeState.Idle) {
                        timerSetUp = globalTimer
                        _state.value = state.value.copy(
                            duration = _state.value.duration.plus(timerSetUp.startTime.seconds),
                            status = TimerStateEnum.Preparing,
                            round = 0
                        )
                    }
                    setStopButton()
                    startForegroundService()
                    startIntervalTimer { }
                }
                ACTION_SERVICE_STOP -> {
                    stopIntervalTimer()
                    setResumeButton()
                }
                ACTION_SERVICE_CANCEL -> {
                    stopIntervalTimer()
                    cancelIntervalTimer()
                    stopForegroundService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("AutoboxingStateValueProperty")
    private fun startIntervalTimer(onTick: () -> Unit) {
        _state.value = state.value.copy(
            currentState = IntervalTimeState.Started
        )
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            _state.value = state.value.copy(
                duration = _state.value.duration.minus(1.seconds)
            )
            updateNotification()
            onTick()
            if (_state.value.duration == ZERO) {
                when (_state.value.status) {
                    TimerStateEnum.Preparing -> {
                        _state.value = state.value.copy(
                            duration = _state.value.duration.plus(timerSetUp.roundTime.seconds),
                            status = TimerStateEnum.Round,
                            round = _state.value.round + 1
                        )
                        playAudio(context, R.raw.bell_soon)
                    }
                    TimerStateEnum.Round -> {
                        if (_state.value.round == timerSetUp.rounds) {
                            stopIntervalTimer()
                            cancelIntervalTimer()
                            stopForegroundService()
                            playAudio(context, R.raw.bell_soon)
                            serviceScope.launch {
                                intervalTimeUseCases.insertIntervalTimeUseCase.invoke(timerSetUp.toIntervalTimer())
                            }
                        } else {
                            _state.value = state.value.copy(
                                duration = _state.value.duration.plus(timerSetUp.delay.seconds),
                                status = TimerStateEnum.Break
                            )
                            playAudio(context, R.raw.bell_finish)
                        }
                    }
                    TimerStateEnum.Break -> {
                        _state.value = state.value.copy(
                            duration = _state.value.duration.plus(timerSetUp.roundTime.seconds),
                            status = TimerStateEnum.Round,
                            round = _state.value.round + 1
                        )
                        playAudio(context, R.raw.bell_soon)
                    }
                }
            }
        }
    }

    private fun stopIntervalTimer() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        _state.value = state.value.copy(
            currentState = IntervalTimeState.Stopped
        )
    }

    private fun cancelIntervalTimer() {
        _state.value = state.value.copy(
            duration = ZERO,
            currentState = IntervalTimeState.Idle
        )
    }

    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun updateNotification() {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setContentText(
                formatTime(
                    seconds = (_state.value.duration.toInt(DurationUnit.SECONDS)%60).pad(),
                    minutes = (_state.value.duration.toInt(DurationUnit.SECONDS)/60).pad()
                ) + "\n${_state.value.round}/${globalTimer.rounds}"
            ).setContentTitle(_state.value.status.name)
                .build()
        )
    }

    @SuppressLint("RestrictedApi")
    private fun setStopButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                "Stop",
                ServiceHelper.stopPendingIntent(this)
            )
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @SuppressLint("RestrictedApi")
    private fun setResumeButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                "Resume",
                ServiceHelper.resumePendingIntent(this)
            )
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
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

    inner class IntervalTimerBinder : Binder() {
        fun getService(): IntervalTimeService = this@IntervalTimeService
    }
}