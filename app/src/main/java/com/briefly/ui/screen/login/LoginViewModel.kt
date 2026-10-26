package com.briefly.ui.screen.login

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel(){
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SignInEvent>()
    val events = _events.asSharedFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = it.validateEmail(value)) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = it.validatePassword(value)) }
    }

    fun onForgotPassword() {
//        viewModelScope.launch { _events.emit(SignInEvent.NavigateToForgotPassword) }
    }

    fun onSignIn() {
        val validated = _uiState.value.validate()
        _uiState.update { validated }

        if (!validated.isValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // Replace with your real API call later
                delay(1500L)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                _events.emit(SignInEvent.NavigateToHome)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Invalid email or password"
                    )
                }
            }
        }
    }

}
sealed class SignInEvent {
    object NavigateToHome           : SignInEvent()
    object NavigateToSignUp         : SignInEvent()
    object NavigateToForgotPassword : SignInEvent()
    data class ShowError(val message: String) : SignInEvent()
}