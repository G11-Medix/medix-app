package com.example.medix.presentation.viewmodels.schedule

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.presentation.ui.state.UiState
import com.example.medix.domain.entities.Appointment
import com.example.medix.domain.repositories.AppointmentRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AppointmentViewModel(
    private val repository: AppointmentRepository
) : ViewModel() {

    var uiState by mutableStateOf<UiState<List<Appointment>>>(UiState.Loading)
        private set

    init {
        loadAppointments()
    }

    fun loadAppointments() {
        viewModelScope.launch {

            uiState = UiState.Loading

            try {
                val data = repository.getAppointments()
                uiState = UiState.Success(data)

            } catch (e: Exception) {
                uiState = UiState.Error("Error al cargar las citas")
            }
        }
    }


    val upcomingAppointments: List<Appointment>
        get() = (uiState as? UiState.Success)?.data
            ?.filter { LocalDateTime.parse(it.date).isAfter(LocalDateTime.now()) }
            ?.sortedBy { LocalDateTime.parse(it.date) }
            ?: emptyList()

    val pastAppointments: List<Appointment>
        get() = (uiState as? UiState.Success)?.data
            ?.filter { LocalDateTime.parse(it.date).isBefore(LocalDateTime.now()) }
            ?.sortedByDescending { LocalDateTime.parse(it.date) }
            ?: emptyList()
}