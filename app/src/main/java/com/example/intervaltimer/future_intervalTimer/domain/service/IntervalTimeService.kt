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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.example.intervaltimer.R
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
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerStateEnum
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
    @Inject
    lateinit var context: Context

    private val binder = StopwatchBinder()

    var duration: MutableState<Duration> = mutableStateOf(Duration.ZERO)
        private set
    private lateinit var timer: Timer

    var status: MutableState<TimerStateEnum> = mutableStateOf(TimerStateEnum.Preparing)
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
                        duration.value = duration.value.plus(timerSetUp.startTime.seconds)
                        status.value = TimerStateEnum.Preparing
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
            duration.value = duration.value.minus(1.seconds)
            updateTimeUnits()
            onTick(minutes.value, seconds.value)
            if (duration.value == Duration.ZERO) {
                when (status.value) {
                    TimerStateEnum.Preparing -> {
                        duration.value = duration.value.plus(timerSetUp.roundTime.seconds)
                        status.value = TimerStateEnum.Round
                        round.value = round.value + 1
                        playAudio(context, R.raw.bell_soon)
                    }
                    TimerStateEnum.Round -> {
                        if (round.value == timerSetUp.rounds) {
                            stopStopwatch()
                            cancelStopwatch()
                            stopForegroundService()
                            playAudio(context, R.raw.bell_soon)
                        } else {
                            duration.value = duration.value.plus(timerSetUp.delay.seconds)
                            status.value = TimerStateEnum.Break
                            playAudio(context, R.raw.gong)
                        }
                    }
                    TimerStateEnum.Break -> {
                        duration.value = duration.value.plus(timerSetUp.roundTime.seconds)
                        status.value = TimerStateEnum.Round
                        round.value = round.value + 1
                        playAudio(context, R.raw.bell_finish)
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
        duration.value = Duration.ZERO
        currentState.value = IntervalTimeState.Idle
        updateTimeUnits()
    }

    private fun updateTimeUnits() {
        duration.value.toComponents { _, minutes, seconds, _ ->
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
            ).setContentTitle(status.value.name)
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

    inner class StopwatchBinder : Binder() {
        fun getService(): IntervalTimeService = this@IntervalTimeService
    }
}