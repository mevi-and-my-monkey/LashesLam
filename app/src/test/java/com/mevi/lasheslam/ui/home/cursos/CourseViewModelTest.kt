package com.mevi.lasheslam.ui.home.cursos

import com.mevi.lasheslam.core.error.AppError
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.AnalyticsTracker
import com.mevi.lasheslam.domain.usecase.CreateCourseUseCase
import com.mevi.lasheslam.domain.usecase.GetCurrentUserIdUseCase
import com.mevi.lasheslam.domain.usecase.GetFavoritesUseCase
import com.mevi.lasheslam.domain.usecase.GetIsAdminUseCase
import com.mevi.lasheslam.domain.usecase.GetIsUserInvitedUseCase
import com.mevi.lasheslam.domain.usecase.GetLocationsUseCase
import com.mevi.lasheslam.domain.usecase.GetNameUserUseCase
import com.mevi.lasheslam.domain.usecase.ToggleFavoriteUseCase
import com.mevi.lasheslam.domain.usecase.courses.CreateCourseRequestUseCase
import com.mevi.lasheslam.domain.usecase.courses.DeleteCourseUseCase
import com.mevi.lasheslam.domain.usecase.courses.GetACourseDetailUseCase
import com.mevi.lasheslam.domain.usecase.courses.GetCourseStatusUseCase
import com.mevi.lasheslam.domain.usecase.courses.UpdateCourseUseCase
import com.mevi.lasheslam.domain.usecase.session.GetEmailUserUseCase
import com.mevi.lasheslam.domain.usecase.session.GetFacebookUseCase
import com.mevi.lasheslam.domain.usecase.session.GetInstagramUseCase
import com.mevi.lasheslam.domain.usecase.session.GetWhatsAppUseCase
import com.mevi.lasheslam.ui.favorites.FavoriteType
import com.mevi.lasheslam.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CourseViewModelTest {

    private val getIsAdminUseCase: GetIsAdminUseCase = mockk(relaxed = true)
    private val getIsUserInvitedUseCase: GetIsUserInvitedUseCase = mockk(relaxed = true)
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase = mockk(relaxed = true)
    private val getNameUserUseCase: GetNameUserUseCase = mockk(relaxed = true)
    private val getEmailUserUseCase: GetEmailUserUseCase = mockk(relaxed = true)
    private val getFavoritesUseCase: GetFavoritesUseCase = mockk(relaxed = true)
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk(relaxed = true)
    private val getFacebookUseCase: GetFacebookUseCase = mockk(relaxed = true)
    private val getInstagramUseCase: GetInstagramUseCase = mockk(relaxed = true)
    private val getWhatsAppUseCase: GetWhatsAppUseCase = mockk(relaxed = true)
    private val getLocationsUseCase: GetLocationsUseCase = mockk(relaxed = true)
    private val createCourseUseCase: CreateCourseUseCase = mockk()
    private val getACourseDetailUseCase: GetACourseDetailUseCase = mockk(relaxed = true)
    private val deleteCourseUseCase: DeleteCourseUseCase = mockk(relaxed = true)
    private val createCourseRequestUseCase: CreateCourseRequestUseCase = mockk()
    private val observeUserCourseStatusUseCase: GetCourseStatusUseCase = mockk(relaxed = true)
    private val updateCourseUseCase: UpdateCourseUseCase = mockk(relaxed = true)
    private val analytics: AnalyticsTracker = mockk(relaxed = true)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun buildViewModel() = CourseViewModel(
        getIsAdminUseCase, getIsUserInvitedUseCase, getCurrentUserIdUseCase, getNameUserUseCase,
        getEmailUserUseCase, getFavoritesUseCase, toggleFavoriteUseCase, getFacebookUseCase,
        getInstagramUseCase, getWhatsAppUseCase, getLocationsUseCase, createCourseUseCase,
        getACourseDetailUseCase, deleteCourseUseCase, createCourseRequestUseCase,
        observeUserCourseStatusUseCase, updateCourseUseCase, analytics
    )

    @Test
    fun `onTitleChange updates both form and update models`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onTitleChange("Curso de Lashista")

        assertEquals("Curso de Lashista", viewModel.uiState.value.form.titulo)
        assertEquals("Curso de Lashista", viewModel.uiState.value.courseUpdate.titulo)
    }

    @Test
    fun `onCostChange accepts numbers and rejects letters`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onCostChange("1500.50")
        assertEquals("1500.50", viewModel.uiState.value.form.costo)

        viewModel.onCostChange("abc")
        // Se mantiene el último valor válido
        assertEquals("1500.50", viewModel.uiState.value.form.costo)
    }

    @Test
    fun `saveCourse emits CourseSaved on success`() = runTest {
        coEvery { createCourseUseCase(any()) } returns Resource.Success(Unit)

        val viewModel = buildViewModel()
        val events = mutableListOf<CourseUiEvent>()
        val job = launch { viewModel.events.collect { events.add(it) } }

        viewModel.saveCourse(selectedLocation = null, linkedBannerIndex = 0)
        advanceUntilIdle()

        assertTrue(events.contains(CourseUiEvent.CourseSaved))
        job.cancel()
    }

    @Test
    fun `saveCourse emits ShowError on failure`() = runTest {
        coEvery { createCourseUseCase(any()) } returns Resource.Error(AppError.Network)

        val viewModel = buildViewModel()
        val events = mutableListOf<CourseUiEvent>()
        val job = launch { viewModel.events.collect { events.add(it) } }

        viewModel.saveCourse(selectedLocation = null, linkedBannerIndex = 0)
        advanceUntilIdle()

        assertTrue(events.any { it is CourseUiEvent.ShowError })
        job.cancel()
    }

    @Test
    fun `createRequest emits RequestCourse on success`() = runTest {
        coEvery { createCourseRequestUseCase(any()) } returns Resource.Success(Unit)

        val viewModel = buildViewModel()
        val events = mutableListOf<CourseUiEvent>()
        val job = launch { viewModel.events.collect { events.add(it) } }

        viewModel.createRequest("course1")
        advanceUntilIdle()

        assertTrue(events.contains(CourseUiEvent.RequestCourse))
        job.cancel()
    }

    @Test
    fun `toggleFavorite does nothing without a logged user`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.toggleFavorite("item1", FavoriteType.COURSE)
        advanceUntilIdle()

        coVerify(exactly = 0) { toggleFavoriteUseCase(any(), any(), any(), any()) }
    }
}
