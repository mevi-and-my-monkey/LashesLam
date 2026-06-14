package com.mevi.lasheslam.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.mevi.lasheslam.data.constants.FirestoreOptions
import com.mevi.lasheslam.data.constants.FirestorePaths
import com.mevi.lasheslam.domain.repository.SessionDataSource
import com.mevi.lasheslam.domain.repository.SessionRepository
import com.mevi.lasheslam.network.LocationItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume

class SessionRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val sessionDataSource: SessionDataSource
) : SessionRepository {
    override fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun getEmail(): String? = firebaseAuth.currentUser?.email

    override fun getUid(): String? = firebaseAuth.currentUser?.uid

    // En un arranque en frío FirebaseAuth restaura el usuario persistido de forma
    // asíncrona, por lo que currentUser puede ser null durante unos milisegundos.
    // El primer callback del AuthStateListener llega cuando el estado ya está
    // inicializado, así que esperamos a él en vez de leer currentUser a ciegas.
    private suspend fun awaitUser(): FirebaseUser? {
        firebaseAuth.currentUser?.let { return it }
        return suspendCancellableCoroutine { cont ->
            lateinit var listener: FirebaseAuth.AuthStateListener
            listener = FirebaseAuth.AuthStateListener { auth ->
                auth.removeAuthStateListener(listener)
                if (cont.isActive) cont.resume(auth.currentUser)
            }
            firebaseAuth.addAuthStateListener(listener)
            cont.invokeOnCancellation { firebaseAuth.removeAuthStateListener(listener) }
        }
    }

    override suspend fun setName() {

        val user = awaitUser() ?: return

        // Se intenta el nombre de Firestore; si la lectura falla o el documento no
        // lo tiene, se usa el displayName local para no dejar el perfil sin nombre.
        val firestoreName = runCatching {
            val snapshot = firestore.collection(FirestorePaths.Users.COLLECTION)
                .document(user.uid)
                .get()
                .await()
            cleanLegacyPasswordFields(snapshot)
            snapshot.getString(FirestorePaths.Users.USER_NAME)
        }.getOrNull()

        val name = firestoreName?.takeIf { it.isNotBlank() } ?: user.displayName

        val firstName = name?.split(" ")?.firstOrNull()?.takeIf { it.isNotBlank() }
        if (firstName != null) {
            sessionDataSource.setNameUser(firstName)
        }
    }

    private suspend fun cleanLegacyPasswordFields(snapshot: DocumentSnapshot) {
        val hasLegacyFields = snapshot.contains(FirestorePaths.Users.LEGACY_PASSWORD) ||
                snapshot.contains(FirestorePaths.Users.LEGACY_CONFIRM_PASSWORD)
        if (!hasLegacyFields) return

        snapshot.reference.update(
            mapOf(
                FirestorePaths.Users.LEGACY_PASSWORD to FieldValue.delete(),
                FirestorePaths.Users.LEGACY_CONFIRM_PASSWORD to FieldValue.delete()
            )
        ).await()
    }

    override suspend fun setPhoto() {
        val user = awaitUser() ?: return
        val googlePhoto = user.photoUrl?.toString()

        val userDoc = firestore.collection(FirestorePaths.Users.COLLECTION).document(user.uid)
        val snapshot = runCatching { userDoc.get().await() }.getOrNull()

        // Si Firestore no responde, al menos se muestra la foto local de la cuenta.
        if (snapshot == null) {
            if (!googlePhoto.isNullOrEmpty()) {
                sessionDataSource.setPhotoUrl(googlePhoto)
            }
            return
        }

        val storedPhoto = snapshot.getString(FirestorePaths.Users.USER_PHOTO)
        val photoUpdatedByUser =
            snapshot.getBoolean(FirestorePaths.Users.PHOTO_UPDATED_BY_USER) ?: false

        // El usuario personalizó su foto: se respeta y no se sincroniza con Google
        if (photoUpdatedByUser) {
            if (!storedPhoto.isNullOrEmpty()) {
                sessionDataSource.setPhotoUrl(storedPhoto)
            }
            return
        }

        if (!googlePhoto.isNullOrEmpty()) {
            if (googlePhoto != storedPhoto) {
                runCatching {
                    userDoc.set(
                        mapOf(FirestorePaths.Users.USER_PHOTO to googlePhoto),
                        FirestoreOptions.MERGE
                    ).await()
                }
            }
            sessionDataSource.setPhotoUrl(googlePhoto)
            return
        }

        if (!storedPhoto.isNullOrEmpty()) {
            sessionDataSource.setPhotoUrl(storedPhoto)
        }
    }

    override fun getUserName(): Flow<String?> {
        return sessionDataSource.nameUser
    }

    override fun getPhotoUrl(): Flow<String?> {
        return sessionDataSource.photoUrl
    }

    override fun getFlowEmail(): Flow<String?> {
        return sessionDataSource.email
    }

    override fun getFacebook(): Flow<String?> {
        return sessionDataSource.facebook
    }

    override fun getInstagram(): Flow<String?> {
        return sessionDataSource.instagram
    }

    override fun getWhatsApp(): Flow<String?> {
        return sessionDataSource.whatsApp
    }


    override suspend fun refreshSession() {
        sessionDataSource.refreshAdmins()
    }

    override fun isAdmin(email: String): Boolean {
        return sessionDataSource.isAdmin(email)
    }

    override fun setAdmin(isAdmin: Boolean) {
        sessionDataSource.setAdmin(isAdmin)
        sessionDataSource.setInvited(false)
    }

    override fun setSessionManager() {
        sessionDataSource.setCurrentUserId(getUid())
        sessionDataSource.setEmailUser(getEmail())
    }

    override fun getIsAdmin(): Flow<Boolean> {
        return sessionDataSource.isUserAdmin
    }

    override fun getIsUserInvited(): Flow<Boolean> {
        return sessionDataSource.isUserInvited
    }

    override fun getCurrentUserId(): Flow<String?> {
        return sessionDataSource.currentUserId
    }

    override fun getLocations(): Flow<List<LocationItem>> {
        return sessionDataSource.locations
    }


}