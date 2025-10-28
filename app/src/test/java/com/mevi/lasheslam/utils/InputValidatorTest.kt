package com.mevi.lasheslam.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class InputValidatorTest {

    @Test
    fun `validateName - empty name should be invalid`() {
        val result = InputValidator.validateName("")
        assertEquals(false, result.isValid)
        assertEquals("El nombre no puede estar vacío", result.errorMessage)
    }

    @Test
    fun `validateName - valid name should be valid`() {
        val result = InputValidator.validateName("John Doe")
        assertEquals(true, result.isValid)
    }

    @Test
    fun `validateEmail - invalid email should be invalid`() {
        val result = InputValidator.validateEmail("invalid-email")
        assertEquals(false, result.isValid)
        assertEquals("Correo inválido", result.errorMessage)
    }

    @Test
    fun `validateEmail - valid email should be valid`() {
        val result = InputValidator.validateEmail("john.doe@example.com")
        assertEquals(true, result.isValid)
    }

    @Test
    fun `validatePassword - short password should be invalid`() {
        val result = InputValidator.validatePassword("12345")
        assertEquals(false, result.isValid)
        assertEquals("Debe tener al menos 8 caracteres, una mayúscula y un número", result.errorMessage)
    }

    @Test
    fun `validatePassword - valid password should be valid`() {
        val result = InputValidator.validatePassword("Contraseña123")
        assertEquals(true, result.isValid)
    }

    @Test
    fun `validateConfirmPassword - passwords do not match should be invalid`() {
        val result = InputValidator.validateConfirmPassword("123456", "654321")
        assertEquals(false, result.isValid)
        assertEquals("Las contraseñas no coinciden", result.errorMessage)
    }

    @Test
    fun `validateConfirmPassword - passwords match should be valid`() {
        val result = InputValidator.validateConfirmPassword("123456", "123456")
        assertEquals(true, result.isValid)
    }

    @Test
    fun `validatePhone - empty phone should be invalid`() {
        val result = InputValidator.validatePhone("")
        assertEquals(false, result.isValid)
        assertEquals("El teléfono no puede estar vacío", result.errorMessage)
    }

    @Test
    fun `validatePhone - phone with less than 10 digits should be invalid`() {
        val result = InputValidator.validatePhone("123456789")
        assertEquals(false, result.isValid)
        assertEquals("Debe tener exactamente 10 dígitos numéricos", result.errorMessage)
    }

    @Test
    fun `validatePhone - valid phone should be valid`() {
        val result = InputValidator.validatePhone("1234567890")
        assertEquals(true, result.isValid)
    }
}