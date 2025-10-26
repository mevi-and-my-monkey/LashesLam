package com.mevi.lasheslam.domain.repository

import com.google.firebase.auth.AuthCredential
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.network.UserModel

interface UserRepository {
    suspend fun signIn(email: String, password: String): Resource<Boolean>
    suspend fun register(user: UserModel): Resource<Boolean>
    suspend fun signInWithGoogle(credential: AuthCredential): Resource<Boolean>
}