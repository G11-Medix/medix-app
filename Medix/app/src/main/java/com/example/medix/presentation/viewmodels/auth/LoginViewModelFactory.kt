package com.example.medix.presentation.viewmodels.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medix.core.network.AuthApi

class LoginViewModelFactory(
    private val api: AuthApi
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(api) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}