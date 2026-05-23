package com.mevi.lasheslam.domain.usecase.products

import com.mevi.lasheslam.domain.model.CreateProductModel
import com.mevi.lasheslam.domain.repository.ProductsRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(private val repository: ProductsRepository) {
    suspend operator fun invoke(product: CreateProductModel) =
        repository.updateProduct(product)
}