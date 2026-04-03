package com.example.medix.presentation.viewmodels.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.medix.data.dto.AppointmentConfirmationDto
import com.example.medix.domain.repositories.ConfirmationRepository
import com.example.medix.presentation.ui.state.UiState


class ConfirmationViewModel(
    private val repository: ConfirmationRepository
) : ViewModel() {

    var uiState by mutableStateOf<UiState<AppointmentConfirmationDto>>(UiState.Loading)
        private set

    fun loadData() {
        viewModelScope.launch {
            uiState = UiState.Loading
            try {
                uiState = UiState.Success(repository.getConfirmation())
            } catch (e: Exception) {
                uiState = UiState.Error("Error al cargar confirmación")
            }
        }
    }
}