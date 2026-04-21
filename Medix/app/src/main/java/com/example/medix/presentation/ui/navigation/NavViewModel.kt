package com.example.medix.presentation.ui.navigation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.core.auth.AuthSessionState

import com.example.medix.core.auth.SessionManager
import com.example.medix.domain.repositories.LegalRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val consentRepository: LegalRepository
) : ViewModel() {

    val sessionState: StateFlow<AuthSessionState> =
        sessionManager.sessionFlow
            .mapLatest { session ->

                if (!session.isLoggedIn) {
                    return@mapLatest session.copy(
                        isLoading = false,
                        hasAcceptedConsent = false
                    )
                }

                val accepted = try {
                    consentRepository.hasAcceptedLatest()
                } catch (e: Exception) {
                    false
                }

                session.copy(
                    isLoading = false,
                    hasAcceptedConsent = accepted
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = AuthSessionState(isLoading = true)
            )
}