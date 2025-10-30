package com.mevi.lasheslam.utils

object InputValidator {

    fun validateName(name: String): ValidationResult {
        val regex = Regex("^[A-Za-z ]+$")
        return when {
            name.isBlank() -> ValidationResult(false, "El nombre no puede estar vacío")
            !regex.matches(name) -> ValidationResult(
                false,
                "El nombre no debe contener caracteres especiales"
            )

            else -> ValidationResult(true)
        }
    }

    fun validateEmail(email: String): ValidationResult {
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        return when {
            email.isBlank() -> ValidationResult(false, "El correo no puede estar vacío")
            !regex.matches(email) -> ValidationResult(false, "Correo inválido")
            else -> ValidationResult(true)
        }
    }

    fun validatePassword(password: String): ValidationResult {
        val regex = Regex("^(?=.*[A-Z])(?=.*\\d).{8,}$")
        return when {
            password.isBlank() -> ValidationResult(false, "La contraseña no puede estar vacía")
            !regex.matches(password) -> ValidationResult(
                false,
                "Debe tener al menos 8 caracteres, una mayúscula y un número"
            )

            else -> ValidationResult(true)
        }
    }

    fun validateConfirmPassword(password: String, confirm: String): ValidationResult {
        return when {
            confirm.isBlank() -> ValidationResult(false, "Confirma tu contraseña")
            confirm != password -> ValidationResult(false, "Las contraseñas no coinciden")
            else -> ValidationResult(true)
        }
    }

    fun validatePhone(phone: String): ValidationResult {
        val regex = Regex("^\\d{10}$")
        return when {
            phone.isBlank() -> ValidationResult(false, "El teléfono no puede estar vacío")
            !regex.matches(phone) -> ValidationResult(
                false,
                "Debe tener exactamente 10 dígitos numéricos"
            )

            else -> ValidationResult(true)
        }
    }

    fun validateStreet(value: String): ValidationResult {
        if (value.isBlank()) return ValidationResult(false, "La calle no puede estar vacía")
        if (value.length < 3) return ValidationResult(false, "Ingresa un nombre de calle válido")
        return ValidationResult(true)
    }

    fun validateExtNumber(value: String): ValidationResult {
        if (value.isBlank()) return ValidationResult(false, "El número exterior es obligatorio")
        if (!value.matches(Regex("^[0-9A-Za-z]+$")))
            return ValidationResult(false, "Solo se permiten letras y números")
        return ValidationResult(true)
    }

    fun validateIntNumber(value: String): ValidationResult {
        // opcional
        if (value.isNotBlank() && !value.matches(Regex("^[0-9A-Za-z]+$"))) {
            return ValidationResult(false, "Solo se permiten letras y números")
        }
        return ValidationResult(true)
    }

    fun validateSuburb(value: String): ValidationResult {
        if (value.isBlank()) return ValidationResult(false, "La colonia no puede estar vacía")
        if (value.length < 3) return ValidationResult(false, "Ingresa una colonia válida")
        return ValidationResult(true)
    }

    fun validateCity(value: String): ValidationResult {
        if (value.isBlank()) return ValidationResult(false, "La ciudad no puede estar vacía")
        if (value.length < 3) return ValidationResult(false, "Ingresa un nombre de ciudad válido")
        return ValidationResult(true)
    }

    fun validatePostalCode(value: String): ValidationResult {
        if (value.isBlank()) return ValidationResult(false, "El código postal es obligatorio")
        if (!value.matches(Regex("^\\d{5}$")))
            return ValidationResult(false, "Debe tener exactamente 5 números")
        return ValidationResult(true)
    }

}


data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)