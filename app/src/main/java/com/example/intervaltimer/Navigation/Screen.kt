package com.example.intervaltimer.Navigation

import com.example.intervaltimer.Model.TimerModel

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
