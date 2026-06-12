package com.mevi.lasheslam.data.constants

object StoragePaths {
    object Users {
        private const val ROOT = "users"
        private const val PROFILE_IMAGE = "profile.jpg"
        fun profilePhoto(userId: String): String {
            return "$ROOT/$userId/$PROFILE_IMAGE"
        }
    }

    object Courses {
        private const val ROOT = "courses"
        private const val COURSE_IMAGE = "course.jpg"
        private const val INSTRUCTOR_IMAGE = "instructor.jpg"
        fun courseImage(courseId: String): String {
            return "$ROOT/$courseId/$COURSE_IMAGE"
        }

        fun instructorImage(courseId: String): String {
            return "$ROOT/$courseId/$INSTRUCTOR_IMAGE"
        }
    }

    object Products {
        private const val ROOT = "products"
        fun productFolder(productId: String): String {
            return "$ROOT/$productId"
        }
    }

    object Services {
        private const val ROOT = "services"
        private const val SERVICE_IMAGE = "service.jpg"
        fun serviceImage(serviceId: String): String {
            return "$ROOT/$serviceId/$SERVICE_IMAGE"
        }
    }
}