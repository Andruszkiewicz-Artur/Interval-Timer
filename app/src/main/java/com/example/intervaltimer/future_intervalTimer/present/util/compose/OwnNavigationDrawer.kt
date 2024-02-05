package com.example.intervaltimer.future_intervalTimer.present.util.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.intervaltimer.R
import com.example.intervaltimer.core.constants.Constants
import com.example.intervaltimer.core.extension.toTime
import com.example.intervaltimer.core.global.globalTimer
import com.example.intervaltimer.future_intervalTimer.domain.model.ChooseOptionEnum
import com.example.intervaltimer.future_intervalTimer.domain.service.ServiceHelper
import com.example.intervaltimer.future_intervalTimer.present.home.HomeEvent
import com.example.intervaltimer.future_intervalTimer.present.home.compose.CurrentChoosePresentation
import com.example.intervaltimer.future_intervalTimer.present.util.navigation.screen.Screen
import kotlinx.coroutines.launch

@Composable
fun OwnNavigationDrawer(
    content: @Composable () -> Unit,
    navHostController: NavHostController,
    menuState: DrawerState
) {
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Text(
                        text = stringResource(id = R.string.Menu),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(16.dp)
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    NavigationDrawerItem(
                        label = {
                            Text(text = stringResource(id = R.string.IntervalTimer))
                        },
                        onClick = {
                            navHostController.navigate(Screen.Home.route)
                            scope.launch {
                                menuState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = null
                            )
                        },
                        selected = navHostController.currentDestination?.route == Screen.Home.route,
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = {
                            Text(text = stringResource(id = R.string.OwnTimers))
                        },
                        onClick = {
                            navHostController.navigate(Screen.OwnIntervalTimers.route)
                            scope.launch {
                                menuState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Timer,
                                contentDescription = null
                            )
                        },
                        selected = navHostController.currentDestination?.route == Screen.OwnIntervalTimers.route,
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = {
                            Text(text = stringResource(id = R.string.History))
                        },
                        onClick = {
                            navHostController.navigate(Screen.History.route)
                            scope.launch {
                                menuState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.History,
                                contentDescription = null
                            )
                        },
                        selected = navHostController.currentDestination?.route == Screen.History.route,
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            },
            drawerState = menuState
        ) {
            content()
        }
    }
}