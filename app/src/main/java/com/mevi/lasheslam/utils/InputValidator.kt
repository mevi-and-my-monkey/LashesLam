package com.mevi.lasheslam.utils

object InputValidator {

    fun validateName(name: String): ValidationResult {
        val regex = Regex("^[A-Za-z ]+$")
        return when {
            name.isBlank() -> ValidationResult(false, "El nombre no puede estar vacío")
            !regex.matches(name) -> ValidationResult(false, "El nombre no debe contener caracteres especiales")
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

}


data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)