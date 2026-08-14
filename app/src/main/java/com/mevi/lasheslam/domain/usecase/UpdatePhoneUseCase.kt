package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.UserRepository
import javax.inject.Inject

class UpdatePhoneUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(phone: String): Resource<Unit> =
        repository.updatePhone(phone)
}
