package com.mevi.lasheslam.utils

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.mevi.lasheslam.ui.auth.LoginViewModel

object Utilities {

    fun isAdmin(loginViewModel: LoginViewModel, email: String): Boolean {
        val adminEmails = loginViewModel.adminEmails.value
        return adminEmails.contains(email)
    }

    fun getDirectDriveImageUrl(originalUrl: String): String {
        val regex = Regex("/d/([a-zA-Z0-9_-]+)")
        val match = regex.find(originalUrl)
        val id = match?.groupValues?.get(1)
        return if (id != null) {
            "https://drive.google.com/uc?export=download&id=$id"
        } else {
            originalUrl // fallback
        }
    }

    fun addItemToCart(productId: String, context: Context) {
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId] ?: 0
                val updatedQuantity = currentQuantity + 1

                val updatedCart = mapOf("cartItems.$productId" to updatedQuantity)

                userDoc.update(updatedCart).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Articulo agregado al carrito", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(context, "Error al agregar al carrito", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    fun removeFromCart(productId: String, context: Context, removeAll: Boolean = false) {
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId] ?: 0
                val updatedQuantity = currentQuantity - 1

                val updatedCart = if (updatedQuantity <= 0 || removeAll) {
                    mapOf("cartItems.$productId" to FieldValue.delete())
                } else {
                    mapOf("cartItems.$productId" to updatedQuantity)
                }

                userDoc.update(updatedCart).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Articulo eliminado del carrito",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(context, "Error al eliminar del carrito", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    fun getDiscountPercentage():Float{
        return 5.0f
    }
    fun getTaxPercentage():Float{
        return 10.0f
    }
}