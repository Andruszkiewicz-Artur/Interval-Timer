package com.example.intervaltimer.future_intervalTimer.present.ownIntervalTimes.compose

import com.example.intervaltimer.future_intervalTimer.domain.model.OwnIntervalTime

sealed class OwnIntervalTimeEvent {
    data class DeleteOwnIntervalTime(val ownIntervalTime: OwnIntervalTime): OwnIntervalTimeEvent()
}
