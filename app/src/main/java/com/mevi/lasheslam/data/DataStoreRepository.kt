package com.mevi.lasheslam.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")
    }

    // ✅ Guardar valores
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

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_DARK_MODE] = enabled
        }
    }

    // ✅ Leer valores como Flow
    val userEmail: Flow<String?> = dataStore.data.map { prefs ->
        prefs[KEY_USER_EMAIL]
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_IS_LOGGED_IN] ?: false
    }

    val darkMode: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_DARK_MODE] ?: false
    }

    // ✅ Borrar todo (logout)
    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}