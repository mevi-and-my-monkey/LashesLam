package com.mevi.lasheslam.ui.profile.students

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.usecase.GetEnrolledCoursesUseCase
import com.mevi.lasheslam.domain.usecase.GetEnrolledStudentsUseCase
import com.mevi.lasheslam.domain.model.EnrolledCourse
import com.mevi.lasheslam.domain.model.EnrolledStudent
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class EnrolledViewModelTest {

    private val getEnrolledCoursesUseCase: GetEnrolledCoursesUseCase = mockk()
    private val getEnrolledStudentsUseCase: GetEnrolledStudentsUseCase = mockk()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    private fun buildViewModel() =
        EnrolledViewModel(getEnrolledCoursesUseCase, getEnrolledStudentsUseCase)

    @Test
    fun `loadCourses publishes the courses returned by the use case`() = runTest {
        val courses = listOf(mockk<EnrolledCourse>())
        coEvery { getEnrolledCoursesUseCase() } returns Resource.Success(courses)

        val viewModel = buildViewModel()
        viewModel.loadCourses()
        advanceUntilIdle()

        assertEquals(courses, viewModel.courses.value)
        assertEquals(false, viewModel.loading.value)
    }

    @Test
    fun `loadCourses leaves courses untouched on error`() = runTest {
        coEvery { getEnrolledCoursesUseCase() } returns Resource.Error(AppError.Network)

        val viewModel = buildViewModel()
        viewModel.loadCourses()
        advanceUntilIdle()

        assertEquals(null, viewModel.courses.value)
        assertEquals(false, viewModel.loading.value)
    }

    @Test
    fun `loadStudents publishes the students for the course`() = runTest {
        val students = listOf(mockk<EnrolledStudent>())
        coEvery { getEnrolledStudentsUseCase("course1") } returns Resource.Success(students)

        val viewModel = buildViewModel()
        viewModel.loadStudents("course1")
        advanceUntilIdle()

        assertEquals(students, viewModel.students.value)
    }
}
