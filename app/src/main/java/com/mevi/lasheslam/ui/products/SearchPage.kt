package com.mevi.lasheslam.ui.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.ui.favorites.FavoriteType
import com.mevi.lasheslam.ui.home.components.HeaderHPCategoriesMenu
import com.mevi.lasheslam.ui.home.components.Section
import com.mevi.lasheslam.ui.home.cursos.CursesListSearch
import com.mevi.lasheslam.ui.home.services.components.ServicesList
import com.mevi.lasheslam.ui.products.search.SearchViewModel
import com.mevi.lasheslam.ui.products.search.lists.ProductsListSearch
import com.mevi.lasheslam.utils.Utilities
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchPage(
    onOpenWhatsApp: (String) -> Unit,
    onNavigateToCourseDetails: (String) -> Unit,
    onNavigateToProductsDetail: (String) -> Unit,
    onNavigateToServiceEdit: (String) -> Unit,
    popBack: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    val favoritesList = viewModel.favorites.collectAsState().value

    val favoriteCourseIds = remember(favoritesList) {
        favoritesList
            .filter { it.type == FavoriteType.COURSE.name }
            .map { it.itemId }
            .toSet()
    }

    val favoriteProductsIds = remember(favoritesList) {
        favoritesList
            .filter { it.type == FavoriteType.PRODUCT.name }
            .map { it.itemId }
            .toSet()
    }

    val favoriteServicesIds = remember(favoritesList) {
        favoritesList
            .filter { it.type == FavoriteType.SERVICE.name }
            .map { it.itemId }
            .toSet()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        AnimatedVisibility(
            visible = true,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    IconButton(
                        onClick = popBack,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.Black
                        )
                    }

                    OutlinedTextField(
                        value = state.query,
                        onValueChange = { viewModel.onSearchChanged(it) },
                        placeholder = {
                            Text("Buscar productos...", color = Color.Black.copy(alpha = 0.8f))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = Color.Black
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.2f)),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedLeadingIconColor = Color.Black,
                            unfocusedLeadingIconColor = Color.Black.copy(alpha = 0.8f),
                            focusedPlaceholderColor = Color.Black.copy(alpha = 0.8f),
                            unfocusedPlaceholderColor = Color.Black.copy(alpha = 0.8f)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                HeaderHPCategoriesMenu(
                    selected = state.selectedSection,
                    onSelect = { viewModel.onSectionSelected(it) }
                )
            }
        }

        LaunchedEffect(Unit) {
            delay(200)
            focusRequester.requestFocus()
        }

        when (state.selectedSection) {
            Section.CURSOS -> {
                CursesListSearch(
                    onNavigateToServiceDetails = onNavigateToCourseDetails,
                    courses = state.filteredCourses,
                    isLoading = state.isLoading,
                    trackEvent = { event ->
                        viewModel.trackEvent(event)
                    },
                    favorites = favoriteCourseIds,
                    onToggleFavorite = { courseId ->
                        viewModel.toggleFavorite(courseId, FavoriteType.COURSE)
                    }
                )
            }

            Section.PRODUCTOS -> {
                ProductsListSearch(
                    products = state.filteredProducts,
                    isLoading = state.isLoading,
                    onNavigateToProductsDetail = onNavigateToProductsDetail,
                    trackEvent = { event ->
                        viewModel.trackEvent(event)
                    },
                    favorites = favoriteProductsIds,
                    onToggleFavorite = { productId ->
                        viewModel.toggleFavorite(productId, FavoriteType.PRODUCT)
                    }
                )
            }

            Section.SERVICIOS -> {
                ServicesList(
                    trackEvent = { event ->
                        viewModel.trackEvent(event)
                    },
                    services = state.filteredServices,
                    isLoading = state.isLoading,
                    onClick = { service ->
                        if (state.isAdmin) {
                            onNavigateToServiceEdit(service.id)
                        }
                    },
                    onClickReservation = { service ->
                        onOpenWhatsApp(
                            Utilities.createServiceMessageWhatsApp(
                                titulo = service.title,
                                precio = service.price.toString(),
                                whatsapp = state.whatsApp.toString()
                            )
                        )
                    }
                )
            }
        }

    }

}