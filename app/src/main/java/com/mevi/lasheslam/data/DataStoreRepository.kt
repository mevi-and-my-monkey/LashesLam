package com.mevi.lasheslam.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mevi.lasheslam.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository @Inject constructor(private val dataStore: DataStore<Preferences>) :
    UserPreferencesRepository {

    companion object {
        val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")
    }

    suspend fun saveUserEmail(email: String) {
        dataStore.edit { prefs ->
            prefs[KEY_USER_EMAIL] = email
        }
    }

    suspend fun setLoggedIn(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_IS_LOGGED_IN] = value
        }
    }

    override suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_DARK_MODE] = enabled
        }
    }

    val userEmail: Flow<String?> = dataStore.data.map { prefs ->
        prefs[KEY_USER_EMAIL]
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_IS_LOGGED_IN] ?: false
    }

    override val darkMode: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_DARK_MODE] ?: false
    }

    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}