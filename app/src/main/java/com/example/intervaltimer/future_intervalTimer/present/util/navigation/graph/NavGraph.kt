package com.example.intervaltimer.future_intervalTimer.present.util

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.intervaltimer.core.constants.Constants
import com.example.intervaltimer.future_intervalTimer.domain.model.TimerModel
import com.example.intervaltimer.future_intervalTimer.domain.service.IntervalTimeService
import com.example.intervaltimer.future_intervalTimer.present.Home
import com.example.intervaltimer.future_intervalTimer.present.TimerPresentation
import com.example.intervaltimer.future_intervalTimer.present.history.compose.HistoryPresent
import com.example.intervaltimer.future_intervalTimer.present.ownIntervalTimes.compose.OwnIntervalTimePresent
import com.example.intervaltimer.future_intervalTimer.present.util.navigation.screen.Screen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    navHostController: NavHostController = rememberNavController(),
    service: IntervalTimeService,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
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
                service = service,
                drawerState = drawerState
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
                navHostController = navHostController,
                drawerState = drawerState
            )
        }

        composable(
            route = Screen.OwnIntervalTimers.route
        ) {
            OwnIntervalTimePresent(
                navHostController = navHostController,
                drawerState = drawerState
            )
        }
    }
}