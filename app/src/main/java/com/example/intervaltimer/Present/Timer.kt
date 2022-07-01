package com.example.intervaltimer.Present

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.intervaltimer.Model.Option
import com.example.intervaltimer.Model.TimerModel
import com.example.intervaltimer.R.drawable.ic_play
import com.example.intervaltimer.R.drawable.ic_pause
import com.example.intervaltimer.ui.theme.Green
import com.example.intervaltimer.ui.theme.Red
import com.example.intervaltimer.ui.theme.Yellow
import kotlinx.coroutines.delay
import com.example.intervaltimer.R.raw.gong
import com.example.intervaltimer.R.raw.bell_finish
import com.example.intervaltimer.R.raw.bell_soon

@Composable
fun Timer(
    navHostController: NavHostController,
    timer: TimerModel
) {
    val context = LocalContext.current
    var rounds by remember { mutableStateOf(timer.rounds) }
    var isWork by remember { mutableStateOf(true) }
    var time by remember { mutableStateOf(timer.startTime) }
    var option by remember { mutableStateOf<Option?>(Option.Start_Time) }

    LaunchedEffect(key1 = time, key2 = isWork) {
        if(isWork) {

            delay(1000L)
            time--

            if(option == Option.Round_Time && time == 10) {
                playAudio(context, bell_soon)
            } else if(time == 0 && option == Option.Round_Time && rounds > 1) {
                playAudio(context, bell_finish)
            }

            if(time == 0 && rounds != 0) {
                when(option) {
                    Option.Start_Time -> {
                        option = Option.Round_Time
                        time = timer.roundTime
                    }
                    Option.Round_Time -> {
                        rounds--
                        option = Option.Delay
                        if(rounds != 0) {
                            time = timer.delay
                        } else {
                            time = 1
                        }
                    }
                    Option.Delay -> {
                        option = Option.Round_Time
                        time = timer.roundTime
                    }
                }
            } else if(rounds == 0 && time == 0) {
                isWork = false
                option = null
                playAudio(context, gong)
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(
                color = when (option) {
                    Option.Start_Time -> {
                        Green
                    }
                    Option.Round_Time -> {
                        if (time > 10) {
                            Green
                        } else {
                            Yellow
                        }
                    }
                    Option.Delay -> {
                        Red
                    }
                    else -> MaterialTheme.colorScheme.background
                }
            )
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = if(isWork) ic_pause else ic_play),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        isWork = !isWork
                        if(option == null) {
                            time = timer.startTime
                            option = Option.Start_Time
                            rounds = timer.rounds
                        }
                    }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Rounds: $rounds"
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "${time/60}: ${if(time%60 < 10) "0" + time%60 else time%60}"
            )
        }
    }
}

fun playAudio(context: Context, id: Int) {

    var mMediaPlayer: MediaPlayer

    try {

        mMediaPlayer = MediaPlayer()
        mMediaPlayer = MediaPlayer.create(context, id)

        mMediaPlayer.setOnPreparedListener {
            mMediaPlayer.start()
        }

        mMediaPlayer.setOnCompletionListener {
            mMediaPlayer.release()
        }

    }
    catch (e: IllegalArgumentException) {
        e.printStackTrace()
    } catch (e: IllegalStateException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}