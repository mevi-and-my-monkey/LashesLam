package com.mevi.lasheslam.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.network.CategoryModel
import com.mevi.lasheslam.ui.components.BottomSheetOption
import com.mevi.lasheslam.ui.components.GenericOptionsBottomSheet
import com.mevi.lasheslam.ui.home.components.CourseAddView
import com.mevi.lasheslam.ui.home.components.HeaderView
import com.mevi.lasheslam.ui.home.components.Section
import com.mevi.lasheslam.ui.home.cursos.CursosPageContent
import com.mevi.lasheslam.ui.home.products.ProductsHPContent
import com.mevi.lasheslam.ui.home.services.ServicesHPContent
import com.mevi.lasheslam.ui.products.add.ProductsAddScreen
import com.mevi.lasheslam.ui.services.add.AddServicesScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePageContent(
    state: HomePageUiState,
    onNavigateToSearch: () -> Unit,
    onNavigateToRequest: () -> Unit,
    onNavigateToRequestUser: () -> Unit,
    onNavigateToServiceDetails: (String) -> Unit,
    onNavigateToServiceDetail: (String) -> Unit,
    onNavigateToProductsDetail: (String) -> Unit,
    onNavigateToServiceEdit: (String) -> Unit,
    onSelectedSection: (Section) -> Unit,
    trackEvent: (AnalyticsEvent) -> Unit,
    trackScreen: (String) -> Unit,
    selectedCategoryId: String?,
    onCategorySelected: (CategoryModel) -> Unit,
    selectedServiceCategoryId: String?,
    onCategoryServiceSelected: (CategoryModel) -> Unit,
    favorites: Set<String>,
    favoritesProducts: Set<String>,
    onToggleFavorite: (String) -> Unit,
    onToggleFavoriteProducts: (String) -> Unit,
    onOpenWhatsApp: (String) -> Unit,
    ) {
    var showOptionsBottomSheet by remember { mutableStateOf(false) }
    var showAddCourseView by remember { mutableStateOf(false) }
    var showAddProductView by remember { mutableStateOf(false) }
    var showAddServiceView by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            HeaderView(
                uiState = state,
                onNavigateToSearch = onNavigateToSearch,
                onNavigateToRequest = onNavigateToRequest,
                onNavigateToRequestUser = onNavigateToRequestUser,
                selectedSection = state.selectedSection,
                onSelectSection = { section ->
                    onSelectedSection(section)
                },
                trackEvent = trackEvent,
                trackScreen = trackScreen,
            )
            when (state.selectedSection) {
                Section.CURSOS -> {
                    CursosPageContent(
                        onNavigateToSearch = onNavigateToSearch,
                        onNavigateToServiceDetails = onNavigateToServiceDetails,
                        courses = state.courses,
                        isLoading = state.isLoading,
                        trackEvent = trackEvent,
                        favorites = favorites,
                        onToggleFavorite = onToggleFavorite
                    )
                }

                Section.PRODUCTOS -> {
                    ProductsHPContent(
                        onNavigateToProductsDetail = onNavigateToProductsDetail,
                        categories = state.categoriesProducts,
                        selectedCategoryId = selectedCategoryId,
                        onCategorySelected = onCategorySelected,
                        products = state.filteredProducts,
                        isLoading = state.isLoading,
                        bestSellingProducts = state.bestSellingProducts,
                        trackEvent = trackEvent,
                        favorites = favoritesProducts,
                        onToggleFavorite = onToggleFavoriteProducts
                    )
                }

                Section.SERVICIOS -> {
                    ServicesHPContent(
                        isAdmin = state.isAdmin,
                        whatsApp = state.whatsApp.toString(),
                        onNavigateToServiceEdit = onNavigateToServiceEdit,
                        onNavigateToServiceDetail = onNavigateToServiceDetail,
                        categories = state.categoriesServices,
                        selectedCategoryId = selectedServiceCategoryId,
                        onCategorySelected = onCategoryServiceSelected,
                        services = state.filteredServices,
                        isLoading = state.isLoading,
                        trackEvent = trackEvent,
                        onOpenWhatsApp = onOpenWhatsApp
                    )
                }

            }

        }

        if (state.isAdmin) {
            FloatingActionButton(
                onClick = {
                    trackEvent(AnalyticsEvent.FloatingHomeClick)
                    trackEvent(AnalyticsEvent.HomeOptionBottomShow)
                    showOptionsBottomSheet = true
                },
                containerColor = Color(0xFFFF80AB),
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add),
                    tint = Color.White
                )
            }
        }

        if (showOptionsBottomSheet) {
            GenericOptionsBottomSheet(
                title = stringResource(R.string.manege_products_and_services),
                onDismiss = {
                    trackEvent(AnalyticsEvent.HomeOptionBottomHide)
                    showOptionsBottomSheet = false
                },
                options = listOf(
                    BottomSheetOption(
                        label = stringResource(R.string.uploaded_new_course),
                        icon = Icons.Default.PostAdd
                    ) {
                        trackEvent(AnalyticsEvent.HomeOptionBottomHide)
                        trackEvent(AnalyticsEvent.AddCourseShow)
                        showOptionsBottomSheet = false
                        showAddCourseView = true
                    },
                    BottomSheetOption(
                        label = stringResource(R.string.uploaded_new_product),
                        icon = Icons.Default.AddBusiness
                    ) {
                        trackEvent(AnalyticsEvent.HomeOptionBottomHide)
                        trackEvent(AnalyticsEvent.AddProductShow)
                        showOptionsBottomSheet = false
                        showAddProductView = true
                    },
                    BottomSheetOption(
                        label = stringResource(R.string.uploaded_new_service),
                        icon = Icons.Default.PostAdd
                    ) {
                        trackEvent(AnalyticsEvent.HomeOptionBottomHide)
                        trackEvent(AnalyticsEvent.AddServiceShow)
                        showOptionsBottomSheet = false
                        showAddServiceView = true
                    }
                )
            )
        }

        if (showAddCourseView) {
            CourseAddView(
                linkedBannerIndex = 99,
                onDismiss = {
                    trackEvent(AnalyticsEvent.AddCourseHide)
                    showAddCourseView = false
                })
        }
        if (showAddProductView) {
            ProductsAddScreen(
                onDismiss = {
                    trackEvent(AnalyticsEvent.AddProductHide)
                    showAddProductView = false
                }
            )
        }
        if (showAddServiceView) {
            AddServicesScreen(
                onDismiss = {
                    trackEvent(AnalyticsEvent.AddServiceHide)
                    showAddServiceView = false
                }
            )
        }
    }
}

