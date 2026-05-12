package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.model.CreateServiceModel
import com.mevi.lasheslam.domain.repository.ServicesRepository
import com.mevi.lasheslam.domain.validation.ServiceValidationMessages
import com.mevi.lasheslam.utils.Utilities.validateRequired
import javax.inject.Inject

class CreateServiceUseCase @Inject constructor(private val repository: ServicesRepository) {
    suspend operator fun invoke(service: CreateServiceModel): Resource<Unit> {
        validateRequired(service.title, ServiceValidationMessages.TITLE_REQUIRED)?.let { return it }
        validateRequired(
            service.duration.toString(),
            ServiceValidationMessages.DURATION_REQUIRED
        )?.let { return it }
        validateRequired(
            service.category,
            ServiceValidationMessages.CATEGORY_REQUIRED
        )?.let { return it }
        validateRequired(
            service.subtitle,
            ServiceValidationMessages.SUBTITLE_REQUIRED
        )?.let { return it }
        validateRequired(
            service.price.toString(),
            ServiceValidationMessages.COST_REQUIRED
        )?.let { return it }
        validateRequired(
            service.image.toString(),
            ServiceValidationMessages.IMAGES_REQUIRED
        )?.let { return it }
        return repository.createService(service)
    }

}