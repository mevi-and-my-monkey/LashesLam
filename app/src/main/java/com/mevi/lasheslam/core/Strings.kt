package com.mevi.lasheslam.core

object Strings {
    const val appName = "LashesLam"
    const val appNameByCreator = "LashesLam by Mevi"
    const val keyRemoteConfigListAdmin = "list_admin"
    const val keyRemoteConfigWhatsappAdmin = "whatsapp_administrador"
    const val keyRemoteConfigInstagramAdmin = "instagram"
    const val keyRemoteConfigFacebookAdmin = "facebook"
    const val keyRemoteConfigLocations = "sucursales"
    const val defaultAdminWhatsapp = "5514023853"
    const val defaultAdminIntagram = "https://instagram.com/"
    const val defaultAdminFacebook = "https://facebook.com/"
    const val logErrorProcessingAdminList = "Error al procesar list_admin"
    const val logErrorFetchingRemoteConfig = "Error al obtener Remote Config"

    object Firestore{
        const val REQUEST_EMPTY = "La solicitud está vacía"
        const val USER_NOT_FOUND = "Usuario no encontrado"
        const val COURSE_NOT_FOUND = "Curso no encontrado"
        const val REQUEST_NOT_FOUND = "Solicitud no encontrada"
    }
}
