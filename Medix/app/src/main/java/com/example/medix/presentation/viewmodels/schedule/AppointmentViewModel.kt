package com.example.medix.presentation.viewmodels.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.domain.entities.Appointment
import com.example.medix.domain.repositories.AppointmentRepository
import com.example.medix.presentation.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val repository: AppointmentRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<List<Appointment>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Appointment>>> = _uiState.asStateFlow()

    init {
        loadAppointments()
    }

    fun loadAppointments() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            runCatching {
                repository.getAppointments()
            }.onSuccess { data ->
                _uiState.value = UiState.Success(data)
            }.onFailure {
                _uiState.value = UiState.Error("Error al cargar las citas")
            }
        }
    }

    val upcomingAppointments: List<Appointment>
        get() = (_uiState.value as? UiState.Success)
            ?.data
            ?.filter { LocalDateTime.parse(it.date).isAfter(LocalDateTime.now()) }
            ?.sortedBy { LocalDateTime.parse(it.date) }
            ?: emptyList()

    val pastAppointments: List<Appointment>
        get() = (_uiState.value as? UiState.Success)
            ?.data
            ?.filter { LocalDateTime.parse(it.date).isBefore(LocalDateTime.now()) }
            ?.sortedByDescending { LocalDateTime.parse(it.date) }
            ?: emptyList()
}