package com.example.medix.presentation.viewmodels.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medix.di.RepositoryModule

class ProfileViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(
                RepositoryModule.provideProfileRepository()
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}