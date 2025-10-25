package com.mevi.lasheslam.ui.auth

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.mevi.lasheslam.core.AuthState
import com.mevi.lasheslam.network.UserModel
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val aut = Firebase.auth
    private val firestore = Firebase.firestore

    var isLoading by mutableStateOf(false)
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var isLoginEnable by mutableStateOf(false)
        private set

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    private val _adminEmails = mutableStateOf(listOf(""))
    val adminEmails: State<List<String>> = _adminEmails

    fun setAdminEmails(emails: List<String>?) {
        _adminEmails.value = emails ?: listOf("")
    }

    fun showLoading() {
        isLoading = true
    }

    fun hideLoading() {
        isLoading = false
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        aut.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                onResult(true, null)
            } else {
                onResult(false, it.exception?.localizedMessage)
            }
        }
    }

    fun signInWithGoogleCredential(
        credential: AuthCredential,
        onResult: (Boolean, String?) -> Unit
    ) = viewModelScope.launch {
        try {
            aut.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userid = task.result?.user?.uid
                    val userModel = UserModel(
                        task.result.user?.displayName ?: "Sin dato",
                        task.result.user?.email ?: "Sin dato",
                        userid!!,
                        task.result.user?.phoneNumber ?: "Sin dato"
                    )
                    val userDoc = firestore.collection("users").document(userid)
                    userDoc.get().addOnSuccessListener { snapshot ->
                        if (!snapshot.exists()) {
                            userDoc.set(userModel)
                                .addOnCompleteListener { response ->
                                    if (response.isSuccessful) {
                                        onResult(true, null)
                                    } else {
                                        onResult(false, "Something went wrong")
                                    }
                                }
                        } else {
                            onResult(true, null)
                        }
                    }
                    onResult(true, null)
                } else {
                    onResult(false, "Fallo la autenticacion con Google")
                }
            }.addOnFailureListener {
                onResult(false, "Fallo la autenticacion con Google")
            }
        } catch (e: Exception) {
            onResult(false, e.localizedMessage)
        }
    }

    fun signUp(
        name: String,
        email: String,
        password: String,
        phone: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        aut.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val userid = it.result?.user?.uid
                val userModel = UserModel(name, email, userid!!, phone)
                val userDoc = firestore.collection("users").document(userid)
                userDoc.get().addOnSuccessListener { document ->
                    if (!document.exists()) {
                        userDoc.set(userModel).addOnCompleteListener { response ->
                            if (response.isSuccessful) {
                                onResult(true, null)
                            } else {
                                onResult(false, "Something went wrong")
                            }
                        }
                    } else {
                        onResult(true, null)
                    }
                }
            } else {
                onResult(false, it.exception?.localizedMessage)
            }
        }
    }

    fun onLoginChanged(email: String, password: String) {
        this.email = email
        this.password = password
        isLoginEnable = enableLogin(email = email, password = password)
    }

    private fun enableLogin(email: String, password: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 6

}