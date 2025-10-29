package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.data.DataStoreRepository
import javax.inject.Inject

class SaveIsDarkModeUseCase @Inject constructor(
    private val repo: DataStoreRepository
) {
    suspend operator fun invoke(isDarkMode: Boolean) {
        repo.setDarkMode(isDarkMode)
    }
}