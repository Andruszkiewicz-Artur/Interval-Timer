package com.example.intervaltimer.future_intervalTimer.present.util

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
                navHostController = navHostController
            )
        }

        composable(
            route = Screen.Timer.route + "/{startTime}/{roundTime}/{delay}/{rounds}",
            arguments = listOf(
                navArgument("startTime") {
                    type = NavType.IntType
                },
                navArgument("roundTime") {
                    type = NavType.IntType
                },
                navArgument("delay") {
                    type = NavType.IntType
                },
                navArgument("rounds") {
                    type = NavType.IntType
                }
            )
        ) {
            TimerPresentation(
                navHostController = navHostController,
                timer = TimerModel(
                    startTime = it.arguments?.getInt("startTime")!!.toInt(),
                    roundTime = it.arguments?.getInt("roundTime")!!.toInt(),
                    delay = it.arguments?.getInt("delay")!!.toInt(),
                    rounds = it.arguments?.getInt("rounds")!!.toInt(),
                ),
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