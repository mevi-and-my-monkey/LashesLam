package com.mevi.lasheslam.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.model.SessionData
import com.mevi.lasheslam.domain.notifications.ObserveUserCoursesUseCase
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.GetCategoriesProducts
import com.mevi.lasheslam.domain.usecase.GetCategoriesServices
import com.mevi.lasheslam.domain.usecase.GetCoursesUseCase
import com.mevi.lasheslam.domain.usecase.GetCurrentUserIdUseCase
import com.mevi.lasheslam.domain.usecase.GetIsAdminUseCase
import com.mevi.lasheslam.domain.usecase.GetIsUserInvitedUseCase
import com.mevi.lasheslam.domain.usecase.GetNameUserUseCase
import com.mevi.lasheslam.domain.usecase.GetPhotoUserUseCase
import com.mevi.lasheslam.domain.usecase.GetProductsUseCase
import com.mevi.lasheslam.domain.usecase.GetRequestsUseCase
import com.mevi.lasheslam.domain.usecase.GetServicesUseCase
import com.mevi.lasheslam.domain.usecase.HandleCourseNotificationsUseCase
import com.mevi.lasheslam.network.CategoryModel
import com.mevi.lasheslam.network.ProductItem
import com.mevi.lasheslam.network.ServiceItem
import com.mevi.lasheslam.ui.home.components.Section
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val analytics: AnalyticsTracker,
    private val getACoursesUseCase: GetCoursesUseCase,
    private val getIsAdminUseCase: GetIsAdminUseCase,
    private val getIsUserInvitedUseCase: GetIsUserInvitedUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getRequestsUseCase: GetRequestsUseCase,
    private val observeUserCoursesUseCase: ObserveUserCoursesUseCase,
    private val handleCourseNotificationsUseCase: HandleCourseNotificationsUseCase,
    private val getNameUserUseCase: GetNameUserUseCase,
    private val getPhotoUserUseCase: GetPhotoUserUseCase,
    private val getCategoriesProducts: GetCategoriesProducts,
    private val getProductsUseCase: GetProductsUseCase,
    private val getCategoriesServices: GetCategoriesServices,
    private val getServicesUseCase: GetServicesUseCase
) : BaseViewModel<HomePageUiState, HomeUiEvent>() {

    override fun createInitialState() = HomePageUiState()

    var selectedCategoryId by mutableStateOf<String?>(FirestorePaths.Products.CATEGORY_ALL)
        private set

    var selectedServiceCategoryId by mutableStateOf<String?>(FirestorePaths.Products.CATEGORY_ALL)
        private set

    init {
        loadCourses()
        observeSession()
    }

    private var isCoursesLoaded = false
    private var isProductsLoaded = false
    private var isServicesLoaded = false

    fun loadCourses() {
        if (isCoursesLoaded) return
        isCoursesLoaded = true

        viewModelScope.launch {
            setState { copy(isLoading = true) }

            val today = Calendar.getInstance().time

            getACoursesUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val filteredCourses = result.data
                            .filter { it.date >= today }
                            .sortedBy { it.date }
                        setState {
                            copy(
                                courses = filteredCourses,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        setState { copy(isLoading = false) }
                        sendError(result.error) { HomeUiEvent.ShowError(it) }
                    }
                }
            }
        }
    }

    fun loadAdminPendingRequests() {
        viewModelScope.launch {
            val result = getRequestsUseCase(FirestorePaths.Courses.STATUS_PANDING)

            when (result) {
                is Resource.Success -> {
                    setState { copy(adminPendingCount = result.data.size) }
                }

                is Resource.Error -> {
                    setState { copy(adminPendingCount = 0) }
                    sendError(result.error) { HomeUiEvent.ShowError(it) }
                }
            }
        }
    }


    private fun observeSession() {
        viewModelScope.launch {
            combine(
                getIsAdminUseCase(),
                getIsUserInvitedUseCase(),
                getCurrentUserIdUseCase()
            ) { isAdmin, isInvited, userId ->
                SessionData(isAdmin, isInvited, userId ?: "")
            }.collect { (isAdmin, isInvited, userId) ->
                setState {
                    copy(
                        isAdmin = isAdmin,
                        isUserInvited = isInvited,
                        currentUserId = userId
                    )
                }
                if (isAdmin) {
                    loadAdminPendingRequests()
                } else if (userId.isNotEmpty()) {
                    observeUserCourses(userId)
                }
            }
        }

        viewModelScope.launch {
            combine(
                getNameUserUseCase(),
                getPhotoUserUseCase()
            ) { name, photo ->
                name to photo
            }.collect { (name, photo) ->
                setState {
                    copy(
                        nameUser = name,
                        photoUser = photo,
                        isProfileLoading = false
                    )
                }
            }
        }
    }

    private var observeJob: Job? = null

    fun observeUserCourses(userId: String) {
        if (observeJob?.isActive == true) return
        observeJob = viewModelScope.launch {
            observeUserCoursesUseCase(userId).collect { courses ->
                handleCourseNotificationsUseCase(userId, courses)
            }
        }
    }

    private var isCategoriesLoaded = false

    fun loadCategories() {
        if (isCategoriesLoaded) return
        isCategoriesLoaded = true

        viewModelScope.launch {
            getCategoriesProducts().collect { result ->
                when (result) {
                    is Resource.Success -> {

                        val sorted = result.data
                            .sortedBy { it.name.lowercase() }

                        val categoriesWithAll = listOf(
                            CategoryModel(id = "all", name = "Todos")
                        ) + sorted

                        setState {
                            copy(categoriesProducts = categoriesWithAll)
                        }
                    }

                    is Resource.Error -> {
                        sendError(result.error) { HomeUiEvent.ShowError(it) }
                    }
                }
            }
        }
    }

    fun onSectionSelected(section: Section) {
        when (section) {
            Section.CURSOS -> {
                trackEvent(AnalyticsEvent.SectionSelected(section.name))
                setState { copy(selectedSection = section) }
            }

            Section.PRODUCTOS -> {
                trackEvent(AnalyticsEvent.SectionSelected(section.name))
                setState { copy(selectedSection = section) }
                loadCategories()
                loadProducts()
            }

            Section.SERVICIOS -> {
                trackEvent(AnalyticsEvent.SectionSelected(section.name))
                setState { copy(selectedSection = section) }
                loadCategoriesService()
                loadServices()
            }
        }
    }

    private fun loadProducts() {
        if (isProductsLoaded) return
        isProductsLoaded = true

        viewModelScope.launch {
            setState { copy(isLoading = true) }

            getProductsUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val products = result.data
                        val bestSelling = products.filter { it.bestSelling }
                        setState {
                            copy(
                                products = products,
                                bestSellingProducts = bestSelling,
                                isLoading = false
                            )
                        }

                        applyFilter(products)
                    }

                    is Resource.Error -> {
                        setState { copy(isLoading = false) }
                        sendError(result.error) { HomeUiEvent.ShowError(it) }
                    }
                }
            }
        }
    }

    fun onCategorySelected(category: CategoryModel) {
        trackEvent(AnalyticsEvent.CategorySelected(category.name))
        selectedCategoryId = category.id
        applyFilter(uiState.value.products)
    }

    private fun applyFilter(products: List<ProductItem>) {
        val filtered =
            if (selectedCategoryId == FirestorePaths.Products.CATEGORY_ALL || selectedCategoryId == null) {
                products
            } else {
                products.filter {
                    it.category.equals(selectedCategoryId, ignoreCase = true)
                }
            }

        setState {
            copy(filteredProducts = filtered)
        }
    }

    fun onCategoryServiceSelected(category: CategoryModel) {
        trackEvent(AnalyticsEvent.CategorySelected(category.name))
        selectedServiceCategoryId = category.id
        applyFilterServices(uiState.value.services)
    }

    private var isCategoriesServiceLoaded = false

    fun loadCategoriesService() {
        if (isCategoriesServiceLoaded) return
        isCategoriesServiceLoaded = true

        viewModelScope.launch {
            getCategoriesServices().collect { result ->
                when (result) {
                    is Resource.Success -> {

                        val sorted = result.data
                            .sortedBy { it.name.lowercase() }

                        val categoriesWithAll = listOf(
                            CategoryModel(id = "all", name = "Todos")
                        ) + sorted

                        setState {
                            copy(categoriesServices = categoriesWithAll)
                        }
                    }

                    is Resource.Error -> {
                        sendError(result.error) { HomeUiEvent.ShowError(it) }
                    }
                }
            }
        }
    }

    private fun loadServices() {
        if (isServicesLoaded) return
        isServicesLoaded = true

        viewModelScope.launch {
            setState { copy(isLoading = true) }

            getServicesUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val services = result.data
                        setState {
                            copy(
                                services = services,
                                isLoading = false
                            )
                        }

                        applyFilterServices(services)
                    }

                    is Resource.Error -> {
                        setState { copy(isLoading = false) }
                        sendError(result.error) { HomeUiEvent.ShowError(it) }
                    }
                }
            }
        }
    }

    private fun applyFilterServices(services: List<ServiceItem>) {
        val filtered =
            if (selectedServiceCategoryId == FirestorePaths.Products.CATEGORY_ALL || selectedServiceCategoryId == null) {
                services
            } else {
                services.filter {
                    it.category.equals(selectedServiceCategoryId, ignoreCase = true)
                }
            }

        setState {
            copy(filteredServices = filtered)
        }
    }

    fun trackEvent(event: AnalyticsEvent) {
        analytics.track(event)
    }

    fun trackScreen(screen: String) {
        analytics.track(AnalyticsEvent.ScreenView(screen))
    }
}