package com.mevi.lasheslam.ui.profile.students

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.data.EnrolledRepositoryImpl
import com.mevi.lasheslam.network.EnrolledCourse
import com.mevi.lasheslam.network.EnrolledStudent
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

    private val repo: EnrolledRepositoryImpl = mockk()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @Test
    fun `loadCourses publishes the courses returned by the repository`() = runTest {
        val courses = listOf(mockk<EnrolledCourse>())
        coEvery { repo.getCourses() } returns Resource.Success(courses)

        val viewModel = EnrolledViewModel(repo)
        viewModel.loadCourses()
        advanceUntilIdle()

        assertEquals(courses, viewModel.courses.value)
        assertEquals(false, viewModel.loading.value)
    }

    @Test
    fun `loadCourses leaves courses untouched on error`() = runTest {
        coEvery { repo.getCourses() } returns Resource.Error(AppError.Network)

        val viewModel = EnrolledViewModel(repo)
        viewModel.loadCourses()
        advanceUntilIdle()

        assertEquals(null, viewModel.courses.value)
        assertEquals(false, viewModel.loading.value)
    }

    @Test
    fun `loadStudents publishes the students for the course`() = runTest {
        val students = listOf(mockk<EnrolledStudent>())
        coEvery { repo.getStudents("course1") } returns Resource.Success(students)

        val viewModel = EnrolledViewModel(repo)
        viewModel.loadStudents("course1")
        advanceUntilIdle()

        assertEquals(students, viewModel.students.value)
    }
}
