package com.mason.todolist.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mason.todolist.dto.LoginResponseDto
import com.mason.todolist.dto.UserRegAndLoginDto
import com.mason.todolist.service.AuthApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Represents the different states of the UI
sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val loginResponse: LoginResponseDto) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(private val authApiService: AuthApiService) : ViewModel() {

    // MutableStateFlow to hold and emit the current UI state
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    // Publicly exposed StateFlow for the UI to observe
    val uiState: StateFlow<AuthUiState> = _uiState

    /**
     * Handles the user login request.
     * @param request The UserRegAndLoginDto containing username and password.
     */
    fun login(request: UserRegAndLoginDto) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val response = authApiService.login(request)
                _uiState.value = AuthUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Login failed")
            }
        }
    }

    /**
     * Handles the user registration request.
     * @param request The UserRegAndLoginDto containing username and password.
     */
    fun register(request: UserRegAndLoginDto) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val response = authApiService.register(request)
                _uiState.value = AuthUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Registration failed")
            }
        }
    }

    /**
     * Resets the UI state to Idle.
     * This is useful after an operation is complete to clear any success or error messages.
     */
    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}