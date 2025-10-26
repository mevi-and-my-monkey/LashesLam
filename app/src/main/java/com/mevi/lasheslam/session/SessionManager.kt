package com.mevi.lasheslam.session

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.mevi.lasheslam.core.Strings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONArray

object SessionManager {

    private const val TAG = "SessionManager"

    private val _isUserAdmin = MutableStateFlow(false)
    val isUserAdmin = _isUserAdmin.asStateFlow()

    private val _isUserInvited = MutableStateFlow(false)
    val isUserInvited = _isUserInvited.asStateFlow()

    private val _whatsApp = MutableStateFlow("")
    val whatsApp = _whatsApp.asStateFlow()

    private var adminEmailsCache: List<String> = emptyList()

    fun setAdmin(value: Boolean) {
        _isUserAdmin.value = value
    }

    fun setInvited(value: Boolean) {
        _isUserInvited.value = value
    }

    fun setWhatsApp(value: String) {
        _whatsApp.value = value
    }

    private fun parseAdminList(jsonString: String): List<String> {
        return try {
            val jsonArray = JSONArray(jsonString)
            List(jsonArray.length()) { i -> jsonArray.getString(i) }
        } catch (e: Exception) {
            Log.e(TAG, Strings.logErrorProcessingAdminList, e)
            emptyList()
        }
    }

    // Consulta Remote Config y actualiza la lista de admins
    suspend fun refreshAdmins() = withContext(Dispatchers.IO) {
        try {
            val remoteConfig = Firebase.remoteConfig
            remoteConfig.fetchAndActivate().await()
            val jsonString = remoteConfig.getString(Strings.keyRemoteConfigListAdmin)
            adminEmailsCache = parseAdminList(jsonString)
            val whatsApp = remoteConfig.getString(Strings.keyRemoteConfigWhatsappAdmin).ifEmpty { Strings.defaultAdminWhatsapp }
            setWhatsApp(whatsApp)
            Log.i("EMAIL_ADMIN", adminEmailsCache.toString())
        } catch (e: Exception) {
            Log.e("SessionManager", Strings.logErrorFetchingRemoteConfig, e)
            adminEmailsCache = emptyList()
        }
    }

    // Verifica si un email es admin
    fun isAdmin(email: String): Boolean {
        Log.i("EMAIL_ADMIN", adminEmailsCache.toString())
        return adminEmailsCache.contains(email)
    }
}