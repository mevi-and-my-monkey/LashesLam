package com.mevi.lasheslam.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mevi.lasheslam.ui.home.HomeScreen
import com.mevi.lasheslam.ui.auth.LogIn
import com.mevi.lasheslam.ui.auth.SplashScreen
import com.mevi.lasheslam.ui.profile.ProfilePage

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Login.route) {
            LogIn(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController, modifier)
        }
        composable(Screen.Profile.route) {
            ProfilePage(navController, modifier)
        }
    }
}