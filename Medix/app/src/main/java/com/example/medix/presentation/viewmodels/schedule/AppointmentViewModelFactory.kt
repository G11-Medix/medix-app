package com.example.medix.presentation.viewmodels.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medix.domain.repositories.AppointmentRepository

class AppointmentViewModelFactory(
    private val repository: AppointmentRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppointmentViewModel(repository) as T
    }
}