package com.example.medix.presentation.viewmodels.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.core.auth.SessionManager
import com.example.medix.data.dto.UserProfileDto
import com.example.medix.domain.repositories.ProfileRepository
import com.example.medix.presentation.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<UserProfileDto>>(UiState.Loading)
    val uiState: StateFlow<UiState<UserProfileDto>> = _uiState.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {

            _uiState.value = UiState.Loading

            runCatching {
                repository.getProfile()
            }.onSuccess { data ->
                _uiState.value = UiState.Success(data)
            }.onFailure { e ->

                if (e is HttpException && e.code() == 401) {
                    launch {
                        sessionManager.clearSession()
                    }
                }

                _uiState.value = UiState.Error("No se pudo cargar el perfil")
            }
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            sessionManager.clearSession()
            onDone()
        }
    }
}