package com.mevi.lasheslam.domain.usecase.products

import com.mevi.lasheslam.domain.repository.ProductsRepository
import javax.inject.Inject

class GetFavoriteProductsUseCase @Inject constructor(
    private val repo: ProductsRepository
) {
    suspend operator fun invoke(ids: List<String>) = repo.getProductsByIds(ids)
}