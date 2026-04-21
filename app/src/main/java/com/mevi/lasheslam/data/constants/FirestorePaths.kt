package com.mevi.lasheslam.data.constants

object FirestorePaths {
    object Users {
        const val COLLECTION = "users"
        fun document(userId: String) = "$COLLECTION/$userId"
    }
}