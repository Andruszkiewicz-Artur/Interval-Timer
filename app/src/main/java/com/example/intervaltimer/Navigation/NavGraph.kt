package com.example.intervaltimer.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.intervaltimer.Model.TimerModel
import com.example.intervaltimer.Present.Home
import com.example.intervaltimer.Present.Timer

@Composable
fun NavGraph(
    navHostController: NavHostController
) {
    val navController: NavHostController
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
            Timer(
                navHostController = navHostController,
                timer = TimerModel(
                    startTime = it.arguments?.getInt("startTime")!!.toInt(),
                    roundTime = it.arguments?.getInt("roundTime")!!.toInt(),
                    delay = it.arguments?.getInt("delay")!!.toInt(),
                    rounds = it.arguments?.getInt("rounds")!!.toInt()
                )
            )
        }
    }
}