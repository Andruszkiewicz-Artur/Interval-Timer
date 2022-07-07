package com.example.intervaltimer.future_intervalTimer.present.util.screen

import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel

sealed class Screen(
    val route: String
) {
    object Home: Screen(
        route = "home"
    )

    object Timer: Screen(
        route = "timer"
    ) {
        fun sendData(
            timer: TimerModel
        ): String {
            return "timer/${timer.startTime}/${timer.roundTime}/${timer.delay}/${timer.rounds}"
        }
    }

}
