package com.example.medix.presentation.viewmodels.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medix.di.RepositoryModule


class ConfirmationViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ConfirmationViewModel::class.java)) {
            return ConfirmationViewModel(
                RepositoryModule.provideConfirmationRepository()
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}