package com.mevi.lasheslam.data.constants

import com.mevi.lasheslam.data.constants.FirestorePaths.Courses.COLLECTION

object FirestorePaths {
    object Users {
        const val COLLECTION = "users"
        const val USER_ID = "userId"
        const val COURSE = "cursos"
        const val USER_PHOTO = "userPhoto"
        const val USER_NAME = "name"
        const val PHOTO_UPDATED_BY_USER = "photoUpdatedByUser"
        const val LEGACY_PASSWORD = "password"
        const val LEGACY_CONFIRM_PASSWORD = "confirmPassword"
        fun document(userId: String) = "$COLLECTION/$userId"
        fun collectionUserRequest(userId: String) = "$COLLECTION/$userId/$COURSE"
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
        fun collectionPath() = "$COLLECTION/$DOCUMENT/$COLLECTION_ITEMS"

        // COURSE MODEL
        const val COURSE_NAME = "courseName"
        const val DATE = "date"
        const val SCHEDULE = "schedule"
        const val NOTIFICATION = "notification"
        const val NOTIFICATION_CREATED = "created"
        const val NOTIFICATION_NOT_CREATED = "notCreated"
        const val TIMESTAMP = "timestamp"
        const val PRICE = "price"
        const val LOCATION = "location"
        const val APARTAR = "apartar"


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
        fun collectionPath() = "$COLLECTION/${DOCUMENT}/$COLLECTION_PRODUCTS_ITEMS"

        // Products categories
        const val CATEGORY_ALL = "all"
    }

    object Services {
        const val COLLECTION_SERVICES = "data"
        const val DOCUMENT = "service"
        const val COLLECTION_CATEGORIES = "categories"
        const val COLLECTION_SERVICES_ITEMS = "services"
        fun collectionPath() = "$COLLECTION/${DOCUMENT}/$COLLECTION_SERVICES_ITEMS"

    }

    object Favorites {
        const val COLLECTION_FAVORITES = "favorites"
    }

    object Booking {
        // Disponibilidad por servicio: service_availability/{serviceId}
        const val AVAILABILITY_COLLECTION = "service_availability"
        // schedule: { "2026-06-19": [{ time, occupied }], ... }
        const val SCHEDULE = "schedule"
        const val SLOT_TIME = "time"
        const val SLOT_OCCUPIED = "occupied"

        const val RESERVATIONS_COLLECTION = "service_reservations"
        const val SERVICE_ID = "serviceId"
        const val DATE = "date"
        const val TIME = "time"
        const val STATUS = "status"
        const val USER_ID = "userId"

        // Estados específicos de reservaciones de servicios
        const val STATUS_PENDING = "pendiente"
        const val STATUS_SCHEDULED = "agendado"
        const val STATUS_CANCELLED = "cancelado"
        const val STATUS_ARCHIVED = "archivado"
    }

    object Orders {
        const val COLLECTION = "product_orders"
        const val STATUS = "status"
        const val USER_ID = "userId"
        const val STATUS_PENDING = "pendiente"
        const val STATUS_COMPLETED = "finalizado"
        const val STATUS_ARCHIVED = "archivado"

        // Órdenes creadas antes del cambio de "aceptado" a "finalizado"
        const val STATUS_LEGACY_ACCEPTED = "aceptado"
    }
}