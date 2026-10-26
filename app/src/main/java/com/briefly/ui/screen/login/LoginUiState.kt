package com.briefly.ui.screen.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null
) {
    val isValid get() = emailError == null && passwordError == null

    fun validateEmail(email: String): String? {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return when {
            email.isBlank() -> null
            !emailRegex.matches(email) -> "Enter a valid email address"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> null
            password.length < 8 -> "At least 8 characters"
            !password.any { it.isUpperCase() } -> "Add a capital letter"
            !password.any { it.isDigit() } -> "Add a number"
            !password.any { !it.isLetterOrDigit() } -> "Add a special character"
            else -> null
        }
    }

    fun validate(): LoginUiState {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return copy(
            emailError = when {
                email.isBlank() -> "Email is required"
                !emailRegex.matches(email) -> "Enter a valid email address"
                else -> null
            },
            passwordError = when {
                password.isBlank() -> "Password is required"
                password.length < 8 -> "At least 8 characters"
                !password.any { it.isUpperCase() } -> "Add a capital letter"
                !password.any { it.isDigit() } -> "Add a number"
                !password.any { !it.isLetterOrDigit() } -> "Add a special character"
                else -> null
            }
        )
    }
}
