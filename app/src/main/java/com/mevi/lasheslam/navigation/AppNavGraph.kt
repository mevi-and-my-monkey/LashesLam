package com.mevi.lasheslam.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mevi.lasheslam.ui.auth.LoginViewModel
import com.mevi.lasheslam.ui.home.HomeScreen
import com.mevi.lasheslam.ui.screens.LogIn
import com.mevi.lasheslam.ui.screens.SplashScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
) {

    val navController = rememberNavController()
    GlobalNavigation.navContoller = navController

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") { SplashScreen(loginViewModel) }
        composable("login") { LogIn(loginViewModel) }
        composable("home") { HomeScreen() }
    }
}

object GlobalNavigation {
    @SuppressLint("StaticFieldLeak")
    lateinit var navContoller: NavHostController
}