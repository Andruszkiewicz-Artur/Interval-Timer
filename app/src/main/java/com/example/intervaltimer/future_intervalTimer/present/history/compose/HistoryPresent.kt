package com.example.intervaltimer.future_intervalTimer.present.history.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.intervaltimer.future_intervalTimer.present.history.HistoryViewModel
import com.example.intervaltimer.R
import com.example.intervaltimer.core.constants.Constants
import com.example.intervaltimer.core.global.globalTimer
import com.example.intervaltimer.future_intervalTimer.domain.service.ServiceHelper
import com.example.intervaltimer.future_intervalTimer.present.util.compose.ExistingTimerPresentation
import com.example.intervaltimer.future_intervalTimer.present.util.compose.ItemPresentation
import com.example.intervaltimer.future_intervalTimer.present.util.compose.OwnNavigationDrawer
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HistoryPresent(
    navHostController: NavHostController,
    viewModel: HistoryViewModel = hiltViewModel(),
    drawerState: DrawerState
) {
    val context = LocalContext.current
    val intervalTimes = viewModel.state.value.intervalTimes
    val scope = rememberCoroutineScope()

    OwnNavigationDrawer(
        content = {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = stringResource(id = R.string.History)
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    if(intervalTimes.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(intervalTimes) { intervalTimer ->
                                ExistingTimerPresentation(
                                    timer = intervalTimer.toTimer(),
                                    onClickStart = {
                                        globalTimer = intervalTimer.toTimer()
                                        ServiceHelper.triggerForegroundService(
                                            context = context,
                                            action = Constants.ACTION_SERVICE_START
                                        )
                                        navHostController.popBackStack()
                                    },
                                    isPossibleToDelete = false
                                )
                            }
                        }
                    } else {
                        Text(
                            text = stringResource(id = R.string.HistoryIsEmpty),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        },
        navHostController = navHostController,
        menuState = drawerState
    )

}