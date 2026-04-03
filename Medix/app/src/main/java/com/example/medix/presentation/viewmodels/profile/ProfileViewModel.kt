package com.example.medix.presentation.viewmodels.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.medix.presentation.ui.state.UiState
import com.example.medix.data.dto.UserProfileDto
import com.example.medix.domain.repositories.ProfileRepository

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
                uiState = UiState.Error("No se pudo cargar el perfil")
            }
        }
    }
}