package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.model.CreateProductModel
import com.mevi.lasheslam.domain.repository.ProductsRepository
import com.mevi.lasheslam.domain.validation.ProductValidationMessages
import com.mevi.lasheslam.utils.Utilities.validateRequired
import javax.inject.Inject

class CreateProductUseCase @Inject constructor(private val repository: ProductsRepository) {
    suspend operator fun invoke(product: CreateProductModel): Resource<Unit> {
        validateRequired(product.title, ProductValidationMessages.TITLE_REQUIRED)?.let { return it }
        validateRequired(
            product.description,
            ProductValidationMessages.DESCRIPTION_REQUIRED
        )?.let { return it }
        validateRequired(
            product.price.toString(),
            ProductValidationMessages.COST_REQUIRED
        )?.let { return it }
        validateRequired(
            product.actulPrice.toString(),
            ProductValidationMessages.ACTUAL_COST_REQUIRED
        )?.let { return it }
        validateRequired(
            product.category,
            ProductValidationMessages.CATEGORY_REQUIRED
        )?.let { return it }
        validateRequired(
            product.images.joinToString(),
            ProductValidationMessages.IMAGES_REQUIRED
        )?.let { return it }
        return repository.createProduct(product)
    }
}