package com.example.medix.presentation.viewmodels.profile

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.presentation.ui.state.UiState
import com.example.medix.data.dto.UserProfileDto
import com.example.medix.domain.repositories.ProfileRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    var uiState by mutableStateOf<UiState<UserProfileDto>>(UiState.Loading)
        private set

    fun loadProfile() {
        viewModelScope.launch {

            uiState = UiState.Loading

            try {
                val data = repository.getProfile()
                uiState = UiState.Success(data)
            } catch (e: Exception) {
                if (e is HttpException && e.code() == 401) {
                    com.example.medix.core.auth.SessionManager.clearSession()
                }
                uiState = UiState.Error("No se pudo cargar el perfil")
            }
        }
    }
}
