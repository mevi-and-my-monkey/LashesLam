package com.mevi.lasheslam

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class User : Application() {

    override fun onCreate() {
        super.onCreate()
        try {
            FirebaseApp.initializeApp(this)
        } catch (e: Exception) {
            Log.e("FirebaseInit", "Error al inicializar Firebase: ${e.message}")
        }
        setupRemoteConfig()
    }

    private fun setupRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig
        val defaultValues = mapOf(
            "list_admin" to "[]",
            "administrador" to ""
        )
        remoteConfig.setDefaultsAsync(defaultValues)
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        Log.i("RemoteConfig", "Firebase Remote Config inicializado")
    }

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        var userInvited: Boolean = false
        var userAdmin: Boolean = false
        var whatsApp: String = ""
    }

}