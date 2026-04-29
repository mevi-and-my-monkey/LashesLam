package com.mevi.lasheslam.domain.usecase

import android.os.Build
import android.util.Log
import com.mevi.lasheslam.data.local.NotificationCache
import com.mevi.lasheslam.domain.model.UserCourse
import com.mevi.lasheslam.domain.repository.NotificationScheduler
import com.mevi.lasheslam.domain.repository.UserCourseRepository
import com.mevi.lasheslam.utils.date.CourseDateParser
import javax.inject.Inject

class HandleCourseNotificationsUseCase @Inject constructor(
    private val repository: UserCourseRepository,
    private val scheduler: NotificationScheduler,
    private val dateParser: CourseDateParser,
    private val cache: NotificationCache
) {
    suspend operator fun invoke(
        userId: String,
        courses: List<UserCourse>
    ): List<UserCourse> {

        val processedCourses = mutableListOf<UserCourse>()

        courses.forEach { course ->

            if (course.notification == "created") return@forEach
            if (!cache.shouldProcess(course.id)) return@forEach
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return@forEach

            val startDateTime = dateParser.parse(course.date, course.schedule)

            scheduler.notifyNow(
                title = "Curso aceptado 🎉",
                message = "Tu curso ${course.name} ha sido aceptado"
            )

            scheduler.schedule(
                courseId = course.id,
                courseName = course.name,
                startDateTime = startDateTime
            )

            try {
                repository.markNotificationAsCreated(userId, course.id)
                processedCourses.add(course)
            } catch (e: Exception) {
                Log.e("Notifications", "Error marking notification", e)
            }

        }

        return processedCourses
    }
}