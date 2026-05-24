package com.mevi.lasheslam.navigation

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mevi.lasheslam.ui.auth.LogIn
import com.mevi.lasheslam.ui.home.HomeScreen
import com.mevi.lasheslam.ui.courses.details.CourseDetailView
import com.mevi.lasheslam.ui.courses.edit.CourseEditView
import com.mevi.lasheslam.ui.products.ProductsView
import com.mevi.lasheslam.ui.products.SearchPage
import com.mevi.lasheslam.ui.products.details.ProductDetailView
import com.mevi.lasheslam.ui.products.edit.ProductEditView
import com.mevi.lasheslam.ui.profile.ProfilePage
import com.mevi.lasheslam.ui.profile.favorite.FavoriteScreen
import com.mevi.lasheslam.ui.profile.request.AdminRequestsScreen
import com.mevi.lasheslam.ui.profile.students.EnrolledCoursesScreen
import com.mevi.lasheslam.ui.profile.students.EnrolledStudentsScreen
import com.mevi.lasheslam.ui.services.edit.ServiceEditView
import com.mevi.lasheslam.ui.splashscreen.SplashScreen
import com.mevi.lasheslam.utils.NavTransitions
import com.mevi.lasheslam.utils.Utilities

@RequiresApi(Build.VERSION_CODES.O)
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
            SplashScreen(
                modifier, onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                })
        }
        composable(Screen.Login.route) {
            LogIn(onNavigateToHome = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                    launchSingleTop = true
                }
            })
        }
        composable(Screen.Home.route) {
            val context = LocalContext.current
            HomeScreen(
                onNavigateToSearch = {
                    navController.navigate(Screen.Search.route) {
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
                    }
                },
                onNavigateToFavorite = {
                    navController.navigate(Screen.Favorite.route) {
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
                    }
                },
                onNavigateToRequest = {
                    navController.navigate(Screen.Request.route) {
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
                    }
                },
                onNavigateToCourses = {
                    navController.navigate(Screen.Courses.route)
                },
                onNavigateToLogOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToCourseDetails = { Id ->
                    navController.navigate(Screen.CourseDetails.createRoute(Id))
                },
                onNavigateToProductsDetail = { Id ->
                    navController.navigate(Screen.ProductDetails.createRoute(Id))
                },
                onNavigateToServiceEdit = { Id ->
                    navController.navigate(Screen.ServiceEdit.createRoute(Id))
                },
                onOpenWhatsApp = { whatsapp ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, whatsapp.toUri()))
                },
                modifier
            )
        }

        composable(
            Screen.Search.route,
            enterTransition = {
                fadeIn() + expandVertically(expandFrom = Alignment.Top)
            },
            exitTransition = {
                fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
            }
        ) {
            val context = LocalContext.current
            SearchPage(
                onNavigateToCourseDetails = { Id ->
                    navController.navigate(Screen.CourseDetails.createRoute(Id))
                },
                onNavigateToProductsDetail = { Id ->
                    navController.navigate(Screen.ProductDetails.createRoute(Id))
                },
                onNavigateToServiceEdit = { Id ->
                    navController.navigate(Screen.ServiceEdit.createRoute(Id))
                },
                onOpenWhatsApp = { whatsapp ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, whatsapp.toUri()))
                },
                popBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Products.route) {
            ProductsView(navController)
        }

        composable(
            route = Screen.Favorite.route,
            enterTransition = NavTransitions.slideIn,
            exitTransition = NavTransitions.slideOut
        ) {
            FavoriteScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfilePage(
                onNavigateToFavorite = {
                    navController.navigate(Screen.Favorite.route) {
                        popUpTo(Screen.Profile.route)
                        launchSingleTop = true
                    }
                },
                onNavigateToRequest = {
                    navController.navigate(Screen.Request.route) {
                        popUpTo(Screen.Profile.route)
                        launchSingleTop = true
                    }
                },
                onNavigateToCourses = {
                    navController.navigate(Screen.Courses.route)
                },
                onNavigateToLogOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }

            )
        }

        /***
         * Secciones dentro de perfil
         */
        composable(
            route = Screen.Request.route,
            enterTransition = NavTransitions.slideIn,
            exitTransition = NavTransitions.slideOut
        ) {
            AdminRequestsScreen(navController)
        }

        composable(
            route = Screen.Courses.route,
            enterTransition = NavTransitions.slideIn,
            exitTransition = NavTransitions.slideOut
        ) {
            EnrolledCoursesScreen(navController)
        }

        composable(
            route = Screen.CourseInscritos.route,
            enterTransition = NavTransitions.slideIn,
            exitTransition = NavTransitions.slideOut
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            val courseName = backStackEntry.arguments?.getString("courseName") ?: ""

            EnrolledStudentsScreen(
                navController = navController,
                courseId = courseId,
                courseName = courseName
            )
        }

        /***
         * Seccion de cursos
         */
        composable(
            route = Screen.CourseDetails.route,
            enterTransition = NavTransitions.slideIn,
            exitTransition = NavTransitions.slideOut
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: return@composable
            val context = LocalContext.current
            CourseDetailView(
                courseId = courseId,
                onDismiss = { navController.popBackStack() },
                onEditClick = { id -> navController.navigate(Screen.CourseEdit.createRoute(id)) },
                onOpenFacebook = { facebook ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, facebook.toUri()))
                },
                onOpenInstagram = { instagram ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, instagram.toUri()))
                },
                onOpenWhatsApp = { whatsapp ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, whatsapp.toUri()))
                },
                onAddToCalendar = { titulo, fecha, horaInicio, horaFin ->
                    Utilities.agregarEventoCalendario(
                        context = context,
                        titulo = titulo,
                        fecha = fecha,
                        horaInicio = horaInicio,
                        horaFin = horaFin
                    )

                },
                modifier = modifier
            )
        }

        composable(
            route = Screen.CourseEdit.route,
            enterTransition = NavTransitions.slideIn,
            exitTransition = NavTransitions.slideOut
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: return@composable
            CourseEditView(
                courseId = courseId,
                onDismiss = { navController.popBackStack() },
                onfinish = {
                    navController.popBackStack(
                        route = Screen.Home.route,
                        inclusive = false
                    )
                }
            )
        }

        /**
         * Seccion de productos
         */
        composable(
            route = Screen.ProductDetails.route,
            enterTransition = NavTransitions.slideIn,
            exitTransition = NavTransitions.slideOut
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: return@composable
            val context = LocalContext.current
            ProductDetailView(
                productId = productId,
                onDismiss = { navController.popBackStack() },
                onEditClick = { id -> navController.navigate(Screen.ProductEdit.createRoute(id)) },
                onOpenFacebook = { facebook ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, facebook.toUri()))
                },
                onOpenInstagram = { instagram ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, instagram.toUri()))
                },
                onOpenWhatsApp = { whatsapp ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, whatsapp.toUri()))
                },
                modifier = modifier
            )
        }

        composable(
            route = Screen.ProductEdit.route,
            enterTransition = NavTransitions.slideIn,
            exitTransition = NavTransitions.slideOut
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: return@composable
            ProductEditView(
                productId = productId,
                onDismiss = { navController.popBackStack() },
                onfinish = {
                    navController.popBackStack(
                        route = Screen.Home.route,
                        inclusive = false
                    )
                }
            )
        }

        /***
         * Seccion de servicios
         */
        composable(
            route = Screen.ServiceDetails.route,
            enterTransition = NavTransitions.slideIn,
            exitTransition = NavTransitions.slideOut
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("serviceId") ?: return@composable
            val context = LocalContext.current
            CourseDetailView(
                courseId = courseId,
                onDismiss = { navController.popBackStack() },
                onEditClick = { id -> navController.navigate(Screen.ServiceEdit.createRoute(id)) },
                onOpenFacebook = { facebook ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, facebook.toUri()))
                },
                onOpenInstagram = { instagram ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, instagram.toUri()))
                },
                onOpenWhatsApp = { whatsapp ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, whatsapp.toUri()))
                },
                onAddToCalendar = { titulo, fecha, horaInicio, horaFin ->
                    Utilities.agregarEventoCalendario(
                        context = context,
                        titulo = titulo,
                        fecha = fecha,
                        horaInicio = horaInicio,
                        horaFin = horaFin
                    )

                },
                modifier = modifier
            )
        }

        composable(
            route = Screen.ServiceEdit.route,
            enterTransition = NavTransitions.slideIn,
            exitTransition = NavTransitions.slideOut
        ) { backStackEntry ->
            val serviceId = backStackEntry.arguments?.getString("serviceId") ?: return@composable
            ServiceEditView(
                serviceId = serviceId,
                onDismiss = { navController.popBackStack() },
                onfinish = {
                    navController.popBackStack(
                        route = Screen.Home.route,
                        inclusive = false
                    )
                }
            )
        }
    }
}