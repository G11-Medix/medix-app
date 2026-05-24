package com.example.medix.presentation.viewmodels.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.core.auth.SessionManager
import com.example.medix.domain.entities.Appointment
import com.example.medix.domain.repositories.AppointmentRepository
import com.example.medix.presentation.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val repository: AppointmentRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<List<Appointment>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Appointment>>> = _uiState.asStateFlow()



    init {
        viewModelScope.launch {
            sessionManager.sessionFlow
                .map { it.pacienteId }
                .filterNotNull()
                .distinctUntilChanged()
                .collectLatest {
                    loadAppointments()
                }
        }
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
            ?.filter { !it.isCancelled() && it.toLocalDateTime().isAfter(LocalDateTime.now()) }
            ?.sortedBy { it.toLocalDateTime() }
            ?: emptyList()

    val pastAppointments: List<Appointment>
        get() = (_uiState.value as? UiState.Success)
            ?.data
            ?.filter { !it.isCancelled() && it.toLocalDateTime().isBefore(LocalDateTime.now()) }
            ?.sortedByDescending { it.toLocalDateTime() }
            ?: emptyList()

    val cancelledAppointments: List<Appointment>
        get() = (_uiState.value as? UiState.Success)
            ?.data
            ?.filter { it.isCancelled() }
            ?.sortedByDescending { it.toLocalDateTime() }
            ?: emptyList()


    fun Appointment.toLocalDateTime(): LocalDateTime {
        val date = LocalDate.parse(this.date)
        val time = LocalTime.parse(this.hour)
        return LocalDateTime.of(date, time)
    }

    private fun Appointment.isCancelled(): Boolean {
        val s = state.trim().lowercase()
        return s in setOf("cancelled", "canceled", "cancelada", "cancelado")
    }
}