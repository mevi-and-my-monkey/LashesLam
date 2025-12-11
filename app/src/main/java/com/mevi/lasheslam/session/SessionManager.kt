package com.mevi.lasheslam.session

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.mevi.lasheslam.core.Strings
import com.mevi.lasheslam.network.LocationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

object SessionManager {

    private const val TAG = "SessionManager"

    private val _isUserAdmin = MutableStateFlow(false)
    val isUserAdmin = _isUserAdmin.asStateFlow()

    private val _isUserInvited = MutableStateFlow(false)
    val isUserInvited = _isUserInvited.asStateFlow()

    private val _whatsApp = MutableStateFlow("")
    val whatsApp = _whatsApp.asStateFlow()

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId = _currentUserId.asStateFlow()

    private val _nameUser = MutableStateFlow<String?>(null)
    val nameUser = _nameUser.asStateFlow()

    private val _emailUser = MutableStateFlow<String?>(null)
    val emailUser = _emailUser.asStateFlow()

    private var adminEmailsCache: List<String> = emptyList()

    private val _locations = MutableStateFlow<List<LocationItem>>(emptyList())
    val locations = _locations.asStateFlow()

    fun setAdmin(value: Boolean) {
        _isUserAdmin.value = value
    }

    fun setInvited(value: Boolean) {
        _isUserInvited.value = value
    }

    fun setWhatsApp(value: String) {
        _whatsApp.value = value
    }

    fun setCurrentUserId(uid: String?) {
        _currentUserId.value = uid
    }

    fun setNameUser(nameUser: String?) {
        _nameUser.value = nameUser
    }

    fun setEmailUser(email: String?) {
        _emailUser.value = email
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

    suspend fun refreshAdmins() = withContext(Dispatchers.IO) {
        try {
            val remoteConfig = Firebase.remoteConfig
            remoteConfig.fetchAndActivate().await()
            val jsonString = remoteConfig.getString(Strings.keyRemoteConfigListAdmin)
            adminEmailsCache = parseAdminList(jsonString)
            val locationsJson = remoteConfig.getString(Strings.keyRemoteConfigLocations)
            _locations.value = parseLocationList(locationsJson)
            val whatsApp = remoteConfig.getString(Strings.keyRemoteConfigWhatsappAdmin)
                .ifEmpty { Strings.defaultAdminWhatsapp }
            setWhatsApp(whatsApp)
            //Log.i("EMAIL_ADMIN", adminEmailsCache.toString())
        } catch (e: Exception) {
            Log.e("SessionManager", Strings.logErrorFetchingRemoteConfig, e)
            adminEmailsCache = emptyList()
        }
    }

    // Verifica si un email es admin
    fun isAdmin(email: String): Boolean {
        //Log.i("EMAIL_ADMIN", adminEmailsCache.toString())
        return adminEmailsCache.contains(email)
    }

    private fun parseLocationList(json: String): List<LocationItem> {
        return try {
            val root = JSONObject(json)
            val array = root.getJSONArray("locations")

            (0 until array.length()).map { i ->
                val obj = array.getJSONObject(i)
                LocationItem(
                    name = obj.getString("name"),
                    lat = obj.getDouble("lat"),
                    lng = obj.getDouble("lng")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}