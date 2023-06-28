package com.example.intervaltimer.future_intervalTimer.present.util.screen

import com.example.intervaltimer.core.constants.Constants
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel

sealed class Screen(
    val route: String
) {
    object Home: Screen(
        route = "home"
    )

    object Timer: Screen(
        route = "timer"
    )

    object History: Screen(
        route = "history"
    )

    object OwnIntervalTimers: Screen(
        route = "ownIntervalTimers"
    )
}
