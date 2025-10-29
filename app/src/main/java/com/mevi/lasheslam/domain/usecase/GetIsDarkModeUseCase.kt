package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.data.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIsDarkModeUseCase @Inject constructor(
    private val repo: DataStoreRepository
) {
    operator fun invoke(): Flow<Boolean> = repo.darkMode
}