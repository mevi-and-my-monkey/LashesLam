package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.ServicesRepository
import javax.inject.Inject

class GetCategoriesServices @Inject constructor(
    private val repo: ServicesRepository
) {
    operator fun invoke() = repo.getCategories()
}