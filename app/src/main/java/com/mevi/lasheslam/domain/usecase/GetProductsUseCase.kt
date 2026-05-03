package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.ProductsRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repo: ProductsRepository
) {
    operator fun invoke() = repo.getAllProducts()
}