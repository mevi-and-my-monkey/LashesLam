package com.mevi.lasheslam.data.constants

import com.mevi.lasheslam.data.constants.FirestorePaths.Courses.COLLECTION
import com.mevi.lasheslam.domain.model.CategoryDefaults
import com.mevi.lasheslam.domain.model.CourseNotificationStatus
import com.mevi.lasheslam.domain.model.CourseRequestStatus
import com.mevi.lasheslam.domain.model.OrderStatus
import com.mevi.lasheslam.domain.model.ReservationStatus

object FirestorePaths {
    object Users {
        const val COLLECTION = "users"
        const val USER_ID = "userId"
        const val COURSE = "cursos"
        const val USER_PHOTO = "userPhoto"
        const val USER_NAME = "name"
        const val ADDRESS = "address"
        const val PHONE = "phone"
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
        val STATUS_PANDING = CourseRequestStatus.PENDING.value
        val STATUS_ACCEPTED = CourseRequestStatus.ACCEPTED.value
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
        val NOTIFICATION_CREATED = CourseNotificationStatus.CREATED.value
        val NOTIFICATION_NOT_CREATED = CourseNotificationStatus.NOT_CREATED.value
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
        const val STOCK = "stock"
        fun collectionPath() = "$COLLECTION/${DOCUMENT}/$COLLECTION_PRODUCTS_ITEMS"

        // Products categories
        const val CATEGORY_ALL = CategoryDefaults.ALL
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
        const val DEPOSIT = "deposit"

        // Estados específicos de reservaciones de servicios
        val STATUS_PENDING_DEPOSIT = ReservationStatus.PENDING_DEPOSIT.value
        val STATUS_PENDING = ReservationStatus.PENDING.value
        val STATUS_SCHEDULED = ReservationStatus.SCHEDULED.value
        val STATUS_CANCELLED = ReservationStatus.CANCELLED.value
        val STATUS_ARCHIVED = ReservationStatus.ARCHIVED.value
    }

    object Orders {
        const val COLLECTION = "product_orders"
        const val STATUS = "status"
        const val USER_ID = "userId"
        val STATUS_PENDING = OrderStatus.PENDING.value
        val STATUS_COMPLETED = OrderStatus.COMPLETED.value
        val STATUS_ARCHIVED = OrderStatus.ARCHIVED.value

        // Órdenes creadas antes del cambio de "aceptado" a "finalizado"
        val STATUS_LEGACY_ACCEPTED = OrderStatus.LEGACY_ACCEPTED.value
    }
}