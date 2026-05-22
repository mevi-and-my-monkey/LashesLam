package com.mevi.lasheslam.domain.usecase.products

import com.mevi.lasheslam.domain.repository.ProductsRepository
import javax.inject.Inject

class GetAProductDetailUseCase @Inject constructor(
private val repo: ProductsRepository
) {
    suspend operator fun invoke(productId: String) = repo.getProductById(productId = productId)
}