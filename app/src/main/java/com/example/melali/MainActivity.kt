package com.example.melali

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.melali.presentation.detail.DetailScreen
import com.example.melali.presentation.home.HomeScreen
import com.example.melali.presentation.list.ListScreen
import com.example.melali.presentation.login.LoginScreen
import com.example.melali.presentation.profile.ProfileScreen
import com.example.melali.presentation.register.RegisterScreen
import com.example.melali.presentation.scheduling.SchedulingScreen
import com.example.melali.util.SnackbarHandler
import com.example.melali.presentation.splash.SplashScreen
import com.example.melali.ui.theme.MelaliTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val _mainViewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainUiViewModel = _mainViewModel

        setContent {
            MelaliTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val snackbarHostState = remember { SnackbarHostState() }
                    val pullRefreshState = rememberPullRefreshState(
                        refreshing = mainUiViewModel.showLoading.value || mainUiViewModel.showRefresh.value,
                        onRefresh = {
                            mainUiViewModel.showRefresh.value = true
                        }
                    )

                    navController.addOnDestinationChangedListener { _, dest, _ ->
                        dest.route?.let { route ->
                            when (route) {
                                "home",
                                "penjadwalan",
                                "penginapan",
                                "restoran",
                                "profil" -> mainUiViewModel.showBottombar.value = true

                                else -> mainUiViewModel.showBottombar.value = false
                            }
                        }
                    }

                    BackHandler {
                        if (mainUiViewModel.backClicked.value) finish()
                        else mainUiViewModel.backClicked.value = true
                    }

                    LaunchedEffect(key1 = mainUiViewModel.showSnackbar.value) {
                        if (mainUiViewModel.showSnackbar.value) {
                            val snackbar = snackbarHostState.showSnackbar(
                                message = mainUiViewModel.snackbarMessage.value,
                                actionLabel = mainUiViewModel.snackbarActionText.value,
                                duration = SnackbarDuration.Short
                            )

                            when (snackbar) {
                                SnackbarResult.Dismissed -> mainUiViewModel.resetSnackbarData()
                                SnackbarResult.ActionPerformed -> {
                                    mainUiViewModel.snackbarAction.value()
                                    mainUiViewModel.resetSnackbarData()
                                }
                            }
                        } else {
                            snackbarHostState.currentSnackbarData?.let {
                                it.dismiss()
                                mainUiViewModel.resetSnackbarData()
                            }
                        }
                    }

                    LaunchedEffect(key1 = mainUiViewModel.backClicked.value) {
                        if (mainUiViewModel.backClicked.value) {
                            SnackbarHandler.showSnackbar(
                                "Klik kembali sekali lagi untuk keluar",
                                "Tutup",
                                action = {
                                    mainUiViewModel.backClicked.value = false
                                }
                            )
                            delay(3000)
                            mainUiViewModel.backClicked.value = false
                        }
                    }

                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState)
                        }
                    ) {
                        Box(modifier = Modifier.padding(it)){
                            NavHost(
                                navController = navController,
                                startDestination = "home"
                            ) {
                                composable("splash") {
                                    SplashScreen(navController = navController)
                                }

                                composable("home"){
                                    HomeScreen(navController = navController)
                                }

                                composable("list"){
                                    ListScreen(navController = navController)
                                }
                                composable("login"){
                                    LoginScreen(navController = navController)
                                }
                                
                                composable("signup"){
                                    RegisterScreen(navController = navController)
                                }

                                composable("login"){
                                    LoginScreen(navController = navController)
                                }

                                composable("register"){
                                    RegisterScreen(navController = navController)
                                }
                                composable("profile"){
                                    ProfileScreen(navController = navController)
                                }
                                composable(
                                    "detail/{index}",
                                    arguments = listOf(
                                        navArgument("index"){
                                            type = NavType.LongType
                                        }
                                    )
                                ){
                                    val index = it.arguments?.getLong("index") ?: 0L

                                    DetailScreen(navController = navController, index = index)
                                }

                                composable("scheduling"){
                                    SchedulingScreen(navController = navController)
                                }
                            }

                            PullRefreshIndicator(
                                modifier = Modifier.align(Alignment.TopCenter),
                                refreshing = mainUiViewModel.showLoading.value || mainUiViewModel.showRefresh.value,
                                state = pullRefreshState
                            )
                        }
                    }
                }
            }
        }
    }
}