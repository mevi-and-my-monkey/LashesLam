package com.mevi.lasheslam.session

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class SessionManagerTest {

    @Test
    fun `setters expose values through their flows`() {
        SessionManager.setCurrentUserId("uid-123")
        SessionManager.setNameUser("Laura")
        SessionManager.setEmailUser("laura@test.com")
        SessionManager.setPhotoUrl("http://photo")
        SessionManager.setAdmin(true)
        SessionManager.setInvited(true)

        assertEquals("uid-123", SessionManager.currentUserId.value)
        assertEquals("Laura", SessionManager.nameUser.value)
        assertEquals("laura@test.com", SessionManager.emailUser.value)
        assertEquals("http://photo", SessionManager.photoUrl.value)
        assertTrue(SessionManager.isUserAdmin.value)
        assertTrue(SessionManager.isUserInvited.value)
    }

    // Garantiza que cerrar sesión no deje datos de la cuenta anterior,
    // que es justo lo que provoca que el siguiente usuario vea datos vacíos o ajenos.
    @Test
    fun `clearUserSession wipes every user scoped field`() {
        SessionManager.setCurrentUserId("uid-123")
        SessionManager.setNameUser("Laura")
        SessionManager.setEmailUser("laura@test.com")
        SessionManager.setPhotoUrl("http://photo")
        SessionManager.setAdmin(true)
        SessionManager.setInvited(true)

        SessionManager.clearUserSession()

        assertNull(SessionManager.currentUserId.value)
        assertNull(SessionManager.nameUser.value)
        assertNull(SessionManager.emailUser.value)
        assertNull(SessionManager.photoUrl.value)
        assertFalse(SessionManager.isUserAdmin.value)
        assertFalse(SessionManager.isUserInvited.value)
    }
}
