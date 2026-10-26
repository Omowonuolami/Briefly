package com.briefly.ui.screen.signup

import kotlinx.serialization.Serializable


@Serializable
data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null
){
    val isValid get() = nameError == null && emailError == null && passwordError == null

    fun validateEmail(email: String): String? {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return when {
            email.isBlank() -> null  // ← no error on empty, only when they start typing
            !emailRegex.matches(email) -> "Enter a valid email address"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> null  // ← no error on empty
            password.length < 8 -> "At least 8 characters"
            !password.any { it.isUpperCase() } -> "Add a capital letter"
            !password.any { it.isDigit() } -> "Add a number"
            !password.any { !it.isLetterOrDigit() } -> "Add a special character"
            else -> null
        }
    }

    fun validate(): SignUpUiState {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return copy(
            nameError = if (name.isBlank()) "Name is required" else null,
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
