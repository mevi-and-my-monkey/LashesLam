package com.mevi.lasheslam.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val darkMode: Flow<Boolean>
    suspend fun setDarkMode(enabled: Boolean)
}