package com.example.medix.presentation.viewmodels.schedule


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.domain.entities.Appointment
import com.example.medix.domain.repositories.AppointmentRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


class AppointmentViewModel(
    private val repository: AppointmentRepository
) : ViewModel() {

    var appointments by mutableStateOf<List<Appointment>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadAppointments()
    }

    private fun loadAppointments() {
        viewModelScope.launch {
            isLoading = true
            try {
                appointments  = repository.getAppointments()


            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    val upcomingAppointments: List<Appointment>
        get() = appointments
            .filter { LocalDateTime.parse(it.date).isAfter(LocalDateTime.now()) }
            .sortedBy { LocalDateTime.parse(it.date) }

    val pastAppointments: List<Appointment>
        get() = appointments
            .filter { LocalDateTime.parse(it.date).isBefore(LocalDateTime.now()) }
            .sortedByDescending { LocalDateTime.parse(it.date) }
}