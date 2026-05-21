package com.mevi.lasheslam.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Products : Screen("products")
    object Search : Screen("search")
    object Request : Screen("request")
    object Favorite : Screen("favorite")
    object Courses : Screen("courses")

    object CourseInscritos : Screen("course_inscritos/{courseId}/{courseName}") {
        fun createRoute(courseId: String, courseName: String) =
            "course_inscritos/$courseId/$courseName"
    }

    object Course : Screen("course")

    object CourseDetails : Screen("course_details/{courseId}") {
        fun createRoute(courseId: String) = "course_details/$courseId"
    }

    object CourseEdit : Screen("course_edit/{courseId}") {
        fun createRoute(courseId: String) = "course_edit/$courseId"
    }

    object Product : Screen("product")

    object ProductDetails : Screen("product_details/{productId}") {
        fun createRoute(productId: String) = "product_details/$productId"
    }

    object ProductEdit : Screen("product_edit/{productId}") {
        fun createRoute(productId: String) = "product_edit/$productId"
    }

    object Service : Screen("service")

    object ServiceDetails : Screen("service_details/{serviceId}") {
        fun createRoute(serviceId: String) = "service_details/$serviceId"
    }

    object ServiceEdit : Screen("service_edit/{serviceId}") {
        fun createRoute(serviceId: String) = "service_edit/$serviceId"
    }
}