package com.mevi.lasheslam.domain.repository

import android.net.Uri
import com.google.firebase.auth.AuthCredential
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.model.UserModel

interface UserRepository {
    suspend fun signIn(email: String, password: String): Resource<Boolean>
    suspend fun register(user: UserModel): Resource<Boolean>
    suspend fun signInWithGoogle(credential: AuthCredential): Resource<Boolean>
    suspend fun updateProfilePhoto(imageUri: Uri): Resource<String>

    /** Perfil del usuario autenticado con la foto ya resuelta (proveedor vs. personalizada). */
    suspend fun getUserProfile(): Resource<UserModel>
    suspend fun updateAddress(address: String): Resource<Unit>
    suspend fun updatePhone(phone: String): Resource<Unit>
    fun signOut()
}
