package com.example.medix.presentation.ui.navigation


import androidx.lifecycle.ViewModel

import com.example.medix.core.auth.SessionManager

import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    val sessionState = sessionManager.sessionFlow
}