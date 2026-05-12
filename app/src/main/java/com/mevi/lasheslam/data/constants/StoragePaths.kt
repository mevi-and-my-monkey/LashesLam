package com.mevi.lasheslam.data.constants

object StoragePaths {
    object Courses {
        private const val ROOT = "services"
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
        private const val PRODUCT_IMAGE = "product.jpg"
        fun productImage(productId: String): String {
            return "$ROOT/$productId/$PRODUCT_IMAGE"
        }
    }
}