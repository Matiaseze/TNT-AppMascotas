package com.appmascotasv2.smartpaws.presentation.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.appmascotasv2.smartpaws.domain.usecase.auth.LoginUseCase
import com.appmascotasv2.smartpaws.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean   = false,
    val error: String?       = null,
    val loggedUserId: Int?   = null,
    val isRegisterMode: Boolean = false
)

class LoginViewModel(
    private val loginUseCase:    LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun toggleMode() {
        _uiState.value = _uiState.value.copy(
            isRegisterMode = !_uiState.value.isRegisterMode,
            error          = null
        )
    }

    fun login(username: String, password: String) {
        if (!validate(username, password)) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            loginUseCase(username.trim(), password)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading    = false,
                        loggedUserId = user.id
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error     = e.message
                    )
                }
        }
    }

    fun register(username: String, password: String, confirmPassword: String) {
        if (!validate(username, password, confirmPassword)) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            registerUseCase(username.trim(), password)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading    = false,
                        loggedUserId = user.id
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error     = e.message
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun validate(
        username: String,
        password: String,
        confirmPassword: String? = null
    ): Boolean = when {
        username.isBlank() -> {
            _uiState.value = _uiState.value.copy(error = "Ingresá un usuario")
            false
        }
        password.length < 6 -> {
            _uiState.value = _uiState.value.copy(
                error = "La contraseña debe tener al menos 6 caracteres"
            )
            false
        }
        confirmPassword != null && password != confirmPassword -> {
            _uiState.value = _uiState.value.copy(error = "Las contraseñas no coinciden")
            false
        }
        else -> true
    }
}

class LoginViewModelFactory(
    private val loginUseCase:    LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LoginViewModel(loginUseCase, registerUseCase) as T
    }
}