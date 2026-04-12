package com.example.medix.presentation.viewmodels.confirmation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.data.dto.AppointmentConfirmationDto
import com.example.medix.domain.repositories.ConfirmationRepository
import com.example.medix.presentation.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmationViewModel @Inject constructor(
    private val repository: ConfirmationRepository
) : ViewModel() {

    var uiState by mutableStateOf<UiState<AppointmentConfirmationDto>>(UiState.Loading)
        private set

    fun loadData() {
        viewModelScope.launch {
            uiState = UiState.Loading
            try {
                val data = repository.getConfirmation()
                uiState = UiState.Success(data)
            } catch (e: Exception) {
                uiState = UiState.Error("Error al cargar confirmación")
            }
        }
    }
}