package com.example.intervaltimer.core.global

import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel

var globalTimer = TimerModel(
    startTime = 15,
    roundTime = 180,
    delay = 60,
    rounds = 5
)