package com.mevi.lasheslam.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Products : Screen("products")
    object Search : Screen("search")
    object ServiceDetails : Screen("service_details/{serviceId}") {
        fun createRoute(serviceId: String) = "service_details/$serviceId"
    }
    object ServiceEdit : Screen("service_edit/{serviceId}") {
        fun createRoute(serviceId: String) = "service_edit/$serviceId"
    }
    object Request : Screen("request")
    object Courses : Screen("courses")

    object CourseInscritos : Screen("course_inscritos/{courseId}/{courseName}") {
        fun createRoute(courseId: String, courseName: String) =
            "course_inscritos/$courseId/$courseName"
    }

}