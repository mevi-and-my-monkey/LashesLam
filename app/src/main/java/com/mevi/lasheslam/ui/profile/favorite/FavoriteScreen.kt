package com.mevi.lasheslam.ui.profile.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.ui.components.GenericLoading
import com.mevi.lasheslam.ui.favorites.FavoriteProductScreen
import com.mevi.lasheslam.ui.favorites.FavoriteServiceScreen
import com.mevi.lasheslam.ui.favorites.FavoritesViewModel
import com.mevi.lasheslam.ui.home.components.Section
import com.mevi.lasheslam.ui.profile.favorite.components.HeaderViewFav

@Composable
fun FavoriteScreen(
    popBack: () -> Unit,
    onNavigateToCourseDetails: (String) -> Unit,
    onNavigateToProductsDetail: (String) -> Unit,
    onNavigateToServiceEdit: (String) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            HeaderViewFav(
                selectedSection = uiState.selectedSection,
                popBack = popBack,
                photoUrl = uiState.photoUser,

                onSelectSection = { section ->
                    viewModel.onSectionSelected(section)
                }
            )

            when (uiState.selectedSection) {
                Section.CURSOS -> FavoriteCoursesScreen(
                    favoriteCourses = uiState.favoriteCourses,
                    onNavigateToCourseDetails = onNavigateToCourseDetails
                )

                Section.PRODUCTOS -> {
                    FavoriteProductScreen(
                        favoriteProducts = uiState.favoriteProducts,
                        onNavigateToProductsDetail = onNavigateToProductsDetail
                    )
                }

                Section.SERVICIOS -> {
                    FavoriteServiceScreen(
                        favoriteServices = uiState.favoriteServices,
                        onNavigateToServiceEdit = onNavigateToServiceEdit
                    )
                }

                else -> {}
            }
        }

        GenericLoading(
            isLoading = uiState.isLoading,
            message = "Procesando, por favor espera...",
            modifier = Modifier.fillMaxSize()
        )
    }
}