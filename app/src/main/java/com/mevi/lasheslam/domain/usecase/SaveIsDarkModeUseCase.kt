package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SaveIsDarkModeUseCase @Inject constructor(
    private val repo: UserPreferencesRepository
) {
    suspend operator fun invoke(isDarkMode: Boolean) {
        repo.setDarkMode(isDarkMode)
    }
}