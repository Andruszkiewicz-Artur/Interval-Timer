package com.example.intervaltimer.future_intervalTimer.present.util

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.intervaltimer.core.constants.Constants
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel
import com.example.intervaltimer.future_intervalTimer.domain.service.IntervalTimeService
import com.example.intervaltimer.future_intervalTimer.present.Home
import com.example.intervaltimer.future_intervalTimer.present.TimerPresentation
import com.example.intervaltimer.future_intervalTimer.present.history.compose.HistoryPresent
import com.example.intervaltimer.future_intervalTimer.present.ownIntervalTimes.compose.OwnIntervalTimePresent
import com.example.intervaltimer.future_intervalTimer.present.util.screen.Screen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    navHostController: NavHostController,
    service: IntervalTimeService
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route
        ) {
            Home(
                navHostController = navHostController,
                service = service
            )
        }

        composable(
            route = Screen.Timer.route
        ) {
            TimerPresentation(
                navHostController = navHostController,
                service = service
            )
        }
        composable(
            route = Screen.History.route
        ) {
            HistoryPresent(
                navHostController = navHostController
            )
        }

        composable(
            route = Screen.OwnIntervalTimers.route
        ) {
            OwnIntervalTimePresent(
                navHostController = navHostController
            )
        }
    }
}