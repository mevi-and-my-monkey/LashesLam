package com.mevi.lasheslam.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Products : Screen("products")
    object Search : Screen("search")
}