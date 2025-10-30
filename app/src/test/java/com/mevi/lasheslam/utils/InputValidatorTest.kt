package com.mevi.lasheslam.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class InputValidatorTest {
    // ---------- NAME ----------
    @Test
    fun `validateName - empty name should be invalid`() {
        val result = InputValidator.validateName("")
        assertEquals(false, result.isValid)
        assertEquals("El nombre no puede estar vacío", result.errorMessage)
    }

    @Test
    fun `validateName - name with special chars should be invalid`() {
        val result = InputValidator.validateName("John@Doe")
        assertEquals(false, result.isValid)
        assertEquals("El nombre no debe contener caracteres especiales", result.errorMessage)
    }

    @Test
    fun `validateName - valid name should be valid`() {
        val result = InputValidator.validateName("John Doe")
        assertEquals(true, result.isValid)
    }

    // ---------- EMAIL ----------
    @Test
    fun `validateEmail - empty email should be invalid`() {
        val result = InputValidator.validateEmail("")
        assertEquals(false, result.isValid)
        assertEquals("El correo no puede estar vacío", result.errorMessage)
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

    // ---------- PASSWORD ----------
    @Test
    fun `validatePassword - empty password should be invalid`() {
        val result = InputValidator.validatePassword("")
        assertEquals(false, result.isValid)
        assertEquals("La contraseña no puede estar vacía", result.errorMessage)
    }

    @Test
    fun `validatePassword - short password should be invalid`() {
        val result = InputValidator.validatePassword("12345")
        assertEquals(false, result.isValid)
        assertEquals(
            "Debe tener al menos 8 caracteres, una mayúscula y un número",
            result.errorMessage
        )
    }

    @Test
    fun `validatePassword - valid password should be valid`() {
        val result = InputValidator.validatePassword("Contraseña123")
        assertEquals(true, result.isValid)
    }

    // ---------- CONFIRM PASSWORD ----------
    @Test
    fun `validateConfirmPassword - empty confirm should be invalid`() {
        val result = InputValidator.validateConfirmPassword("123456", "")
        assertEquals(false, result.isValid)
        assertEquals("Confirma tu contraseña", result.errorMessage)
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

    // ---------- PHONE ----------
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

    // ---------- STREET ----------
    @Test
    fun `validateStreet - empty street should be invalid`() {
        val result = InputValidator.validateStreet("")
        assertEquals(false, result.isValid)
        assertEquals("La calle no puede estar vacía", result.errorMessage)
    }

    @Test
    fun `validateStreet - short street should be invalid`() {
        val result = InputValidator.validateStreet("Av")
        assertEquals(false, result.isValid)
        assertEquals("Ingresa un nombre de calle válido", result.errorMessage)
    }

    @Test
    fun `validateStreet - valid street should be valid`() {
        val result = InputValidator.validateStreet("Avenida Reforma")
        assertEquals(true, result.isValid)
    }

    // ---------- EXT NUMBER ----------
    @Test
    fun `validateExtNumber - empty should be invalid`() {
        val result = InputValidator.validateExtNumber("")
        assertEquals(false, result.isValid)
        assertEquals("El número exterior es obligatorio", result.errorMessage)
    }

    @Test
    fun `validateExtNumber - invalid characters should be invalid`() {
        val result = InputValidator.validateExtNumber("12#A")
        assertEquals(false, result.isValid)
        assertEquals("Solo se permiten letras y números", result.errorMessage)
    }

    @Test
    fun `validateExtNumber - valid number should be valid`() {
        val result = InputValidator.validateExtNumber("12A")
        assertEquals(true, result.isValid)
    }

    // ---------- INT NUMBER ----------
    @Test
    fun `validateIntNumber - empty should be valid`() {
        val result = InputValidator.validateIntNumber("")
        assertEquals(true, result.isValid)
    }

    @Test
    fun `validateIntNumber - invalid characters should be invalid`() {
        val result = InputValidator.validateIntNumber("A#3")
        assertEquals(false, result.isValid)
        assertEquals("Solo se permiten letras y números", result.errorMessage)
    }

    @Test
    fun `validateIntNumber - valid should be valid`() {
        val result = InputValidator.validateIntNumber("B12")
        assertEquals(true, result.isValid)
    }

    // ---------- SUBURB ----------
    @Test
    fun `validateSuburb - empty should be invalid`() {
        val result = InputValidator.validateSuburb("")
        assertEquals(false, result.isValid)
        assertEquals("La colonia no puede estar vacía", result.errorMessage)
    }

    @Test
    fun `validateSuburb - short name should be invalid`() {
        val result = InputValidator.validateSuburb("ab")
        assertEquals(false, result.isValid)
        assertEquals("Ingresa una colonia válida", result.errorMessage)
    }

    @Test
    fun `validateSuburb - valid suburb should be valid`() {
        val result = InputValidator.validateSuburb("Centro")
        assertEquals(true, result.isValid)
    }

    // ---------- CITY ----------
    @Test
    fun `validateCity - empty should be invalid`() {
        val result = InputValidator.validateCity("")
        assertEquals(false, result.isValid)
        assertEquals("La ciudad no puede estar vacía", result.errorMessage)
    }

    @Test
    fun `validateCity - short should be invalid`() {
        val result = InputValidator.validateCity("Cd")
        assertEquals(false, result.isValid)
        assertEquals("Ingresa un nombre de ciudad válido", result.errorMessage)
    }

    @Test
    fun `validateCity - valid should be valid`() {
        val result = InputValidator.validateCity("Ciudad de México")
        assertEquals(true, result.isValid)
    }

    // ---------- POSTAL CODE ----------
    @Test
    fun `validatePostalCode - empty should be invalid`() {
        val result = InputValidator.validatePostalCode("")
        assertEquals(false, result.isValid)
        assertEquals("El código postal es obligatorio", result.errorMessage)
    }

    @Test
    fun `validatePostalCode - invalid format should be invalid`() {
        val result = InputValidator.validatePostalCode("12AB3")
        assertEquals(false, result.isValid)
        assertEquals("Debe tener exactamente 5 números", result.errorMessage)
    }

    @Test
    fun `validatePostalCode - valid code should be valid`() {
        val result = InputValidator.validatePostalCode("12345")
        assertEquals(true, result.isValid)
    }
}