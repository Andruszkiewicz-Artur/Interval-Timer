package com.example.intervaltimer.future_intervalTimer.domain.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.example.intervaltimer.core.constants.Constants.ACTION_SERVICE_CANCEL
import com.example.intervaltimer.core.constants.Constants.ACTION_SERVICE_START
import com.example.intervaltimer.core.constants.Constants.ACTION_SERVICE_STOP
import com.example.intervaltimer.core.constants.Constants.NOTIFICATION_CHANNEL_ID
import com.example.intervaltimer.core.constants.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.intervaltimer.core.constants.Constants.NOTIFICATION_ID
import com.example.intervaltimer.core.constants.Constants.STOPWATCH_STATE
import com.example.intervaltimer.core.extensions.formatTime
import com.example.intervaltimer.core.extensions.pad
import com.example.intervaltimer.core.global.globalTimer
import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTimeState
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel
import com.example.intervaltimer.future_intervalTimer.present.timer.TimerStateEnum
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@ExperimentalAnimationApi
@AndroidEntryPoint
class IntervalTimeService : Service() {
    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    private val binder = StopwatchBinder()

    var duration: Duration = Duration.ZERO
        private set
    private lateinit var timer: Timer

    var status: TimerStateEnum = TimerStateEnum.Preparing
        private set

    private var timerSetUp: TimerModel = globalTimer
    private var isLoaded: Boolean = false
    var round = mutableIntStateOf(0)
        private set
    var seconds = mutableStateOf("00")
        private set
    var minutes = mutableStateOf("00")
        private set
    var currentState = mutableStateOf(IntervalTimeState.Idle)
        private set

    override fun onBind(p0: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra(STOPWATCH_STATE)) {
            IntervalTimeState.Started.name -> {
                setStopButton()
                startForegroundService()
                startStopwatch { minutes, seconds ->
                    updateNotification(minutes = minutes, seconds = seconds)
                }
            }
            IntervalTimeState.Stopped.name -> {
                stopStopwatch()
                setResumeButton()
            }
            IntervalTimeState.Canceled.name -> {
                stopStopwatch()
                cancelStopwatch()
                stopForegroundService()
                isLoaded = false
            }
        }
        intent?.action.let {
            when (it) {
                ACTION_SERVICE_START -> {
                    if(!isLoaded) {
                        isLoaded = true
                        timerSetUp = globalTimer
                        duration = duration.plus(timerSetUp.startTime.seconds)
                        status = TimerStateEnum.Preparing
                    }
                    setStopButton()
                    startForegroundService()
                    startStopwatch { minutes, seconds ->
                        updateNotification(minutes = minutes, seconds = seconds)
                    }
                }
                ACTION_SERVICE_STOP -> {
                    stopStopwatch()
                    setResumeButton()
                }
                ACTION_SERVICE_CANCEL -> {
                    stopStopwatch()
                    cancelStopwatch()
                    stopForegroundService()
                    isLoaded = false
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("AutoboxingStateValueProperty")
    private fun startStopwatch(onTick: (m: String, s: String) -> Unit) {
        currentState.value = IntervalTimeState.Started
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.minus(1.seconds)
            updateTimeUnits()
            onTick(minutes.value, seconds.value)
            if (duration == Duration.ZERO) {
                when (status) {
                    TimerStateEnum.Preparing -> {
                        duration = duration.plus(timerSetUp.roundTime.seconds)
                        status = TimerStateEnum.Round
                        round.value = round.value + 1
                    }
                    TimerStateEnum.Round -> {
                        if (round.value == timerSetUp.rounds) {
                            stopStopwatch()
                            cancelStopwatch()
                            stopForegroundService()
                        } else {
                            duration = duration.plus(timerSetUp.delay.seconds)
                            status = TimerStateEnum.Break
                        }
                    }
                    TimerStateEnum.Break -> {
                        duration = duration.plus(timerSetUp.roundTime.seconds)
                        status = TimerStateEnum.Round
                        round.value = round.value + 1
                    }
                }
            }
        }
    }

    private fun stopStopwatch() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        currentState.value = IntervalTimeState.Stopped
    }

    private fun cancelStopwatch() {
        duration = Duration.ZERO
        currentState.value = IntervalTimeState.Idle
        updateTimeUnits()
    }

    private fun updateTimeUnits() {
        duration.toComponents { _, minutes, seconds, _ ->
            this@IntervalTimeService.minutes.value = minutes.pad()
            this@IntervalTimeService.seconds.value = seconds.pad()
        }
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

    @SuppressLint("AutoboxingStateValueProperty")
    private fun updateNotification(minutes: String, seconds: String) {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setContentText(
                formatTime(
                    seconds, minutes
                ) + "\n${round.value}/${globalTimer.rounds}"
            ).setContentTitle(status.name)
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

    inner class StopwatchBinder : Binder() {
        fun getService(): IntervalTimeService = this@IntervalTimeService
    }
}