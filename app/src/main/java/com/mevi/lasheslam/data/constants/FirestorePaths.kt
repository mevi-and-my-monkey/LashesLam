package com.mevi.lasheslam.data.constants

object FirestorePaths {
    object Users {
        const val COLLECTION = "users"
        const val USER_ID = "userId"
        const val COURSE = "cursos"
        const val USER_PHOTO = "userPhoto"
        const val USER_NAME = "name"
        fun document(userId: String) = "$COLLECTION/$userId"
    }

    object Courses {
        const val COLLECTION = "data"
        const val DOCUMENT = "curse"
        const val COLLECTION_ITEMS = "items"
        const val STATUS_PANDING = "pendiente"
        const val STATUS_ACCEPTED = "aceptado"
        const val STATUS = "status"
        const val COURSES_REQUESTS = "course_requests"
        const val COURSE_ID = "courseId"
        const val REQUEST = "solicitar"
        fun document() = "$COLLECTION/$DOCUMENT"

        // COURSE MODEL
        const val COURSE_NAME = "courseName"
        const val DATE = "date"
        const val SCHEDULE = "schedule"
        const val NOTIFICATION = "notification"
        const val NOTIFICATION_CREATED = "created"
        const val NOTIFICATION_NOT_CREATED = "notCreated"
        const val TIMESTAMP = "timestamp"


        // COURSE REQUEST
        const val STUDENTS_ENROLLED = "alumnos_inscritos"
        const val ENROLLED = "inscritos"
        const val REQUEST_ID = "requestId"
    }

    object Products {
        const val COLLECTION_PRODUCTS = "data"
        const val DOCUMENT = "stock"
        const val COLLECTION_CATEGORIES = "categories"
        const val COLLECTION_PRODUCTS_ITEMS = "products"

        // Products categories
        const val CATEGORY_ALL = "all"
    }

    object Services {
        const val COLLECTION_SERVICES = "data"
        const val DOCUMENT = "service"
        const val COLLECTION_CATEGORIES = "categories"
        const val COLLECTION_SERVICES_ITEMS = "service"

    }
}