package com.mevi.lasheslam.domain.usecase

import com.mevi.lasheslam.domain.repository.SessionRepository
import com.mevi.lasheslam.domain.usecase.session.GetEmailUserUseCase
import com.mevi.lasheslam.domain.usecase.session.GetFacebookUseCase
import com.mevi.lasheslam.domain.usecase.session.GetInstagramUseCase
import com.mevi.lasheslam.domain.usecase.session.GetWhatsAppUseCase
import com.mevi.lasheslam.network.LocationItem
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertSame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

/**
 * Los "getter" de sesión son delegaciones puras al SessionRepository.
 * Se verifica que cada uno devuelve exactamente el flow del repositorio.
 */
class SessionGettersUseCaseTest {

    private val repository: SessionRepository = mockk()

    @Test
    fun `GetNameUserUseCase delegates to repository`() {
        val flow: Flow<String?> = flowOf("Laura")
        every { repository.getUserName() } returns flow
        assertSame(flow, GetNameUserUseCase(repository)())
    }

    @Test
    fun `GetPhotoUserUseCase delegates to repository`() {
        val flow: Flow<String?> = flowOf("http://photo")
        every { repository.getPhotoUrl() } returns flow
        assertSame(flow, GetPhotoUserUseCase(repository)())
    }

    @Test
    fun `GetCurrentUserIdUseCase delegates to repository`() {
        val flow: Flow<String?> = flowOf("uid-1")
        every { repository.getCurrentUserId() } returns flow
        assertSame(flow, GetCurrentUserIdUseCase(repository)())
    }

    @Test
    fun `GetIsAdminUseCase delegates to repository`() {
        val flow: Flow<Boolean> = flowOf(true)
        every { repository.getIsAdmin() } returns flow
        assertSame(flow, GetIsAdminUseCase(repository)())
    }

    @Test
    fun `GetIsUserInvitedUseCase delegates to repository`() {
        val flow: Flow<Boolean> = flowOf(false)
        every { repository.getIsUserInvited() } returns flow
        assertSame(flow, GetIsUserInvitedUseCase(repository)())
    }

    @Test
    fun `GetLocationsUseCase delegates to repository`() {
        val flow: Flow<List<LocationItem>> = flowOf(emptyList())
        every { repository.getLocations() } returns flow
        assertSame(flow, GetLocationsUseCase(repository)())
    }

    @Test
    fun `GetEmailUserUseCase delegates to repository`() {
        val flow: Flow<String?> = flowOf("a@b.com")
        every { repository.getFlowEmail() } returns flow
        assertSame(flow, GetEmailUserUseCase(repository)())
    }

    @Test
    fun `GetFacebookUseCase delegates to repository`() {
        val flow: Flow<String?> = flowOf("fb")
        every { repository.getFacebook() } returns flow
        assertSame(flow, GetFacebookUseCase(repository)())
    }

    @Test
    fun `GetInstagramUseCase delegates to repository`() {
        val flow: Flow<String?> = flowOf("ig")
        every { repository.getInstagram() } returns flow
        assertSame(flow, GetInstagramUseCase(repository)())
    }

    @Test
    fun `GetWhatsAppUseCase delegates to repository`() {
        val flow: Flow<String?> = flowOf("wa")
        every { repository.getWhatsApp() } returns flow
        assertSame(flow, GetWhatsAppUseCase(repository)())
    }
}
