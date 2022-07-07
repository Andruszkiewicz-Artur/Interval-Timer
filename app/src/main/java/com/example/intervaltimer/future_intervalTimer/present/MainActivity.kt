package com.example.intervaltimer.future_intervalTimer.present

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.intervaltimer.future_intervalTimer.present.util.NavGraph
import com.example.intervaltimer.ui.theme.IntervalTimerTheme

class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()

            IntervalTimerTheme {
                NavGraph(navHostController = navController)
            }
        }
    }
}