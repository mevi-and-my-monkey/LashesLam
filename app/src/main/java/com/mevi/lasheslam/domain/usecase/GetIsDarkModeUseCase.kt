package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDarkModeUseCase @Inject constructor(
    private val repo: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> = repo.darkMode
}