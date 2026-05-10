package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.SessionRepository
import com.mevi.lasheslam.network.LocationItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationsUseCase @Inject constructor(private val repository: SessionRepository) {
    operator fun invoke(): Flow<List<LocationItem>> {
        return repository.getLocations()
    }
}