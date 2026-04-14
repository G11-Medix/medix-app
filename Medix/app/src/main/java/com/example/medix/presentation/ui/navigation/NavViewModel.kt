package com.example.medix.presentation.ui.navigation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.core.auth.AuthSessionState

import com.example.medix.core.auth.SessionManager

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    val sessionState: StateFlow<AuthSessionState> =
        sessionManager.sessionFlow
            .map { session ->
                session.copy(isLoading = false)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = AuthSessionState(isLoading = true)
            )
}