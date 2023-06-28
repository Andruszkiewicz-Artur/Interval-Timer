package com.example.intervaltimer.future_intervalTimer.domain.service

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.net.toUri
import com.example.intervaltimer.core.constants.Constants
import com.example.intervaltimer.core.constants.Constants.CANCEL_REQUEST_CODE
import com.example.intervaltimer.core.constants.Constants.CLICK_REQUEST_CODE
import com.example.intervaltimer.core.constants.Constants.RESUME_REQUEST_CODE
import com.example.intervaltimer.core.constants.Constants.STOPWATCH_STATE
import com.example.intervaltimer.core.constants.Constants.STOP_REQUEST_CODE
import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTimeState
import com.example.intervaltimer.future_intervalTimer.present.MainActivity

@ExperimentalAnimationApi
object ServiceHelper {

    private const val flag = PendingIntent.FLAG_IMMUTABLE

    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(STOPWATCH_STATE, IntervalTimeState.Started.name)
        }
        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, clickIntent, flag
        )
    }

    fun stopPendingIntent(context: Context): PendingIntent {
        val stopIntent = Intent(context, IntervalTimeService::class.java).apply {
            putExtra(STOPWATCH_STATE, IntervalTimeState.Stopped.name)
        }
        return PendingIntent.getService(
            context, STOP_REQUEST_CODE, stopIntent, flag
        )
    }

    fun resumePendingIntent(context: Context): PendingIntent {
        val resumeIntent = Intent(context, IntervalTimeService::class.java).apply {
            putExtra(STOPWATCH_STATE, IntervalTimeState.Started.name)
        }
        return PendingIntent.getService(
            context, RESUME_REQUEST_CODE, resumeIntent, flag
        )
    }

    fun cancelPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, IntervalTimeService::class.java).apply {
            putExtra(STOPWATCH_STATE, IntervalTimeState.Canceled.name)
        }
        return PendingIntent.getService(
            context, CANCEL_REQUEST_CODE, cancelIntent, flag
        )
    }

    fun triggerForegroundService(context: Context, action: String) {
        Intent(context, IntervalTimeService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }
}