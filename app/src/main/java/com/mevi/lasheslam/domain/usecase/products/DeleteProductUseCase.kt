package com.mevi.lasheslam.domain.usecase.products

import com.mevi.lasheslam.domain.repository.ProductsRepository
import javax.inject.Inject


class DeleteProductUseCase @Inject constructor(private val repository: ProductsRepository) {
    suspend operator fun invoke(productId: String, imageUrl: List<String>) =
        repository.deleteCourse(productId = productId, imageUrl = imageUrl)
}