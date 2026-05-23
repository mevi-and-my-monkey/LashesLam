package com.mevi.lasheslam.ui.products

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.BaseViewModel
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.domain.model.CreateProductModel
import com.mevi.lasheslam.domain.model.SessionData
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.CreateProductUseCase
import com.mevi.lasheslam.domain.usecase.GetCategoriesProducts
import com.mevi.lasheslam.domain.usecase.GetCurrentUserIdUseCase
import com.mevi.lasheslam.domain.usecase.GetFavoritesUseCase
import com.mevi.lasheslam.domain.usecase.GetIsAdminUseCase
import com.mevi.lasheslam.domain.usecase.GetIsUserInvitedUseCase
import com.mevi.lasheslam.domain.usecase.GetNameUserUseCase
import com.mevi.lasheslam.domain.usecase.ToggleFavoriteUseCase
import com.mevi.lasheslam.domain.usecase.products.DeleteProductUseCase
import com.mevi.lasheslam.domain.usecase.products.GetAProductDetailUseCase
import com.mevi.lasheslam.domain.usecase.products.UpdateProductUseCase
import com.mevi.lasheslam.domain.usecase.session.GetEmailUserUseCase
import com.mevi.lasheslam.domain.usecase.session.GetFacebookUseCase
import com.mevi.lasheslam.domain.usecase.session.GetInstagramUseCase
import com.mevi.lasheslam.domain.usecase.session.GetWhatsAppUseCase
import com.mevi.lasheslam.network.FavoriteItem
import com.mevi.lasheslam.ui.favorites.FavoriteType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getIsAdminUseCase: GetIsAdminUseCase,
    private val getIsUserInvitedUseCase: GetIsUserInvitedUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getNameUserUseCase: GetNameUserUseCase,
    private val getEmailUserUseCase: GetEmailUserUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getFacebookUseCase: GetFacebookUseCase,
    private val getInstagramUseCase: GetInstagramUseCase,
    private val getWhatsAppUseCase: GetWhatsAppUseCase,
    private val getAProductDetailUseCase: GetAProductDetailUseCase,
    private val getCategoriesProducts: GetCategoriesProducts,
    private val createProductUseCase: CreateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val analytics: AnalyticsTracker,
) : BaseViewModel<ProductsUiState, ProductUiEvent>() {

    override fun createInitialState() = ProductsUiState()

    private val _favorites = MutableStateFlow<List<FavoriteItem>>(emptyList())
    val favorites = _favorites.asStateFlow()

    private var isCategoriesLoaded = false

    fun onTitleChange(title: String) {
        setState { copy(form = form.copy(titulo = title)) }
    }

    fun onCharacteristicsChange(characteristics: String) {
        setState { copy(form = form.copy(caracteristicas = characteristics)) }
    }

    fun onDescriptionChange(description: String) {
        setState { copy(form = form.copy(descripcion = description)) }
    }

    fun onCostChange(cost: String) {
        setState { copy(form = form.copy(precio = cost)) }
    }

    fun onActualCostChange(actualCost: String) {
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

    fun removeRemoteImage(image: String) {
        val updated = uiState.value.form.remoteImages.filterNot { it == image }

        setState {
            copy(
                form = form.copy(
                    remoteImages = updated
                )
            )
        }
    }

    fun onCategoryChange(categoryId: String) {
        setState { copy(form = form.copy(category = categoryId)) }
    }

    fun onBestSellingChange(bestSelling: Boolean) {
        setState { copy(form = form.copy(masVendidos = bestSelling)) }
    }

    init {
        observeSession()
        loadCategories()
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
                if (userId.isNotEmpty()) {
                    loadFavorites(userId)
                }
            }
        }

        viewModelScope.launch {
            combine(
                getNameUserUseCase(),
                getEmailUserUseCase()
            ) { name, email ->
                name to email
            }.collect { (name, email) ->
                setState {
                    copy(
                        nameUser = name,
                        email = email,
                    )
                }
            }
        }

        viewModelScope.launch {
            combine(
                getFacebookUseCase(),
                getInstagramUseCase(),
                getWhatsAppUseCase()
            ) { facebook, instagram, whatsApp ->
                Triple(facebook, instagram, whatsApp)
            }.collect { (facebook, instagram, whatsApp) ->
                setState {
                    copy(
                        facebook = facebook,
                        instagram = instagram,
                        whatsApp = whatsApp
                    )
                }
            }
        }
    }


    fun loadFavorites(userId: String) {
        viewModelScope.launch {
            when (val result = getFavoritesUseCase(userId)) {
                is Resource.Success -> {
                    _favorites.value = result.data
                }

                else -> {}
            }
        }
    }

    fun toggleFavorite(itemId: String, type: FavoriteType) {
        val userId = uiState.value.currentUserId ?: return
        trackEvent(AnalyticsEvent.FavoriteClick(type.name))

        viewModelScope.launch {
            val currentFavorites = _favorites.value
            val isFavorite = currentFavorites.any {
                it.itemId == itemId && it.type == type.name
            }
            when (
                toggleFavoriteUseCase(
                    userId = userId,
                    itemId = itemId,
                    type = type.name,
                    isFavorite = isFavorite
                )
            ) {
                is Resource.Success -> {
                    _favorites.value =
                        if (isFavorite) {
                            currentFavorites.filterNot {
                                it.itemId == itemId && it.type == type.name
                            }
                        } else {
                            currentFavorites + FavoriteItem(itemId, type.name)
                        }
                }

                else -> {}
            }
        }
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
            id = form.id,
            actulPrice = form.precioActual.toDoubleOrNull() ?: 0.0,
            bestSelling = form.masVendidos,
            category = form.category,
            description = form.descripcion,
            price = form.precio.toDoubleOrNull() ?: 0.0,
            title = form.titulo,
            images = form.images,
            characteristics = form.caracteristicas
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

    fun updateProduct() = launchWithLoading {
        trackEvent(AnalyticsEvent.UpdateProductClick)

        val form = uiState.value.form

        val product = CreateProductModel(
            id = form.id,
            actulPrice = form.precioActual.toDoubleOrNull() ?: 0.0,
            bestSelling = form.masVendidos,
            category = form.category,
            description = form.descripcion,
            price = form.precio.toDoubleOrNull() ?: 0.0,
            title = form.titulo,
            images = form.images,
            characteristics = form.caracteristicas,
            remoteImages = form.remoteImages
        )

        when (val result = updateProductUseCase(product)) {

            is Resource.Success -> {
                sendEvent(ProductUiEvent.ProductUpdated)
            }

            is Resource.Error -> {
                sendEvent(ProductUiEvent.ShowError(result.error))
            }
        }
    }

    fun deleteCourse(productId: String, imageUrl: List<String>) {
        viewModelScope.launch {
            when (val result = deleteProductUseCase(productId, imageUrl)) {
                is Resource.Success -> {
                    sendEvent(ProductUiEvent.ProductDeleted)
                }

                is Resource.Error -> {
                    sendEvent(ProductUiEvent.ShowError(result.error))
                }
            }
        }
    }

    fun loadProductById(productId: String) = launchWithLoading {
        when (val result = getAProductDetailUseCase(productId)) {
            is Resource.Success -> {
                val product = result.data
                setState {
                    copy(
                        productDetail = productDetail.copy(
                            id = product.id,
                            title = product.title,
                            characteristics = product.characteristics,
                            description = product.description,
                            price = product.price,
                            actulPrice = product.actulPrice,
                            bestSelling = product.bestSelling,
                            category = product.category,
                            images = product.images
                        ),
                        form = form.copy(
                            id = product.id,
                            titulo = product.title,
                            caracteristicas = product.characteristics,
                            descripcion = product.description,
                            precio = product.price.toString(),
                            precioActual = product.actulPrice.toString(),
                            masVendidos = product.bestSelling,
                            category = product.category,
                            remoteImages = product.images
                        )
                    )
                }
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