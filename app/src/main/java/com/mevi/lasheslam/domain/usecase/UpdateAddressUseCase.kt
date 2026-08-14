package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.UserRepository
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(address: String): Resource<Unit> =
        repository.updateAddress(address)
}
