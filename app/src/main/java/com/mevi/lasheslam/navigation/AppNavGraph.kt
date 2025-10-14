package com.mevi.lasheslam.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mevi.lasheslam.ui.screens.LogIn
import com.mevi.lasheslam.ui.screens.SplashScreen

@Composable
fun AppNavGraph(navController: NavHostController, innerPadding: PaddingValues, ) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LogIn() }
    }
}