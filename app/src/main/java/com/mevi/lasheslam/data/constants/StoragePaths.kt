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
}