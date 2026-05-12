package com.mevi.lasheslam.ui.products

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.model.CreateProductModel
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.CreateProductUseCase
import com.mevi.lasheslam.domain.usecase.GetCategoriesProducts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getCategoriesProducts: GetCategoriesProducts,
    private val createProductUseCase: CreateProductUseCase,
    private val analytics: AnalyticsTracker,
) : BaseViewModel<ProductsUiState, ProductUiEvent>() {

    override fun createInitialState() = ProductsUiState()

    private var isCategoriesLoaded = false

    fun onTitleChange(title: String) {
        setState { copy(form = form.copy(titulo = title)) }
    }

    fun onDescriptionChange(description: String) {
        setState { copy(form = form.copy(descripcion = description)) }
    }

    fun onCostChange(cost: Double) {
        setState { copy(form = form.copy(precio = cost)) }
    }

    fun onActualCostChange(actualCost: Double) {
        setState { copy(form = form.copy(precioActual = actualCost)) }
    }

    fun onImagesSelected(images: List<Uri>) {
        val currentImages = uiState.value.form.images
        val updatedImages = (currentImages + images)
            .distinct()
            .take(5)

        setState { copy(form = form.copy(images = updatedImages)) }
    }

    fun removeImage(image: Uri) {
        val updatedImages = uiState.value.form.images.filterNot { it == image }
        setState { copy(form = form.copy(images = updatedImages)) }
    }

    fun onCategoryChange(categoryId: String) {
        setState { copy(form = form.copy(category = categoryId)) }
    }

    fun onBestSellingChange(bestSelling: Boolean) {
        setState { copy(form = form.copy(masVendidos = bestSelling)) }
    }

    init {
        loadCategories()
    }

    fun loadCategories() {
        if (isCategoriesLoaded) return
        isCategoriesLoaded = true

        viewModelScope.launch {
            getCategoriesProducts().collect { result ->
                when (result) {
                    is Resource.Success -> {

                        val sorted = result.data.sortedBy { it.name.lowercase() }

                        setState {
                            copy(categoriesProducts = sorted)
                        }
                    }

                    is Resource.Error -> {
                        sendError(result.error) { ProductUiEvent.ShowError(it) }
                    }
                }
            }
        }
    }

    fun saveProduct() = launchWithLoading {
        trackEvent(AnalyticsEvent.SaveProductClick)
        val form = uiState.value.form

        val product = CreateProductModel(
            actulPrice = form.precioActual,
            bestSelling = form.masVendidos,
            category = form.category,
            description = form.descripcion,
            price = form.precio,
            title = form.titulo,
            images = form.images
        )
        when (val result = createProductUseCase(product)) {
            is Resource.Success -> {
                sendEvent(ProductUiEvent.ProductSaved)
            }

            is Resource.Error -> {
                sendEvent(ProductUiEvent.ShowError(result.error))
            }
        }

    }

    fun trackEvent(event: AnalyticsEvent) {
        analytics.track(event)
    }

    fun trackScreen(screen: String) {
        analytics.track(AnalyticsEvent.ScreenView(screen))
    }
}