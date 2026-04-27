package com.mevi.lasheslam.data.constants

object FirestorePaths {
    object Users {
        const val COLLECTION = "users"
        fun document(userId: String) = "$COLLECTION/$userId"
    }

    object Courses {
        const val COLLECTION = "data"
        const val DOCUMENT = "curse"
        const val COLLECTION_ITEMS = "items"
        fun document() = "$COLLECTION/$DOCUMENT"
    }
}