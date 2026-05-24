package com.example.medix.presentation.viewmodels.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import com.example.medix.domain.entities.Notification
import com.example.medix.core.auth.SessionManager
import com.example.medix.domain.repositories.NotificationRepository
import com.example.medix.presentation.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@Suppress("unused")
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Notification>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Notification>>> = _uiState

    init {
        // Reload notifications whenever the authenticated paciente changes.
        viewModelScope.launch {
            sessionManager.sessionFlow.collect { session ->
                // clear UI immediately to avoid showing previous user's notifications
                _uiState.value = UiState.Loading
                // load notifications for current session (repository currently returns all local notifications)
                loadNotifications(session.pacienteId)
                markAllAsRead(session.pacienteId)
            }
        }
    }

    fun loadNotifications(pacienteId: Long? = null) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val scopedPacienteId = pacienteId ?: sessionManager.getPacienteId()

            runCatching {
                repository.getNotifications(scopedPacienteId)
            }.onSuccess { data ->
                _uiState.value = UiState.Success(data)
            }.onFailure { e ->
                _uiState.value = UiState.Error(
                    e.message ?: "Error al cargar notificaciones"
                )
            }
        }
    }

    @Suppress("unused")
    fun deleteNotification(id: Int) {
        viewModelScope.launch {
            runCatching {
                repository.deleteNotification(id)
            }.onSuccess {
                loadNotifications()
            }.onFailure {
                // ignore failure for delete, could log
            }
        }
    }

    @Suppress("unused")
    fun markAsRead(id: Int) {
        viewModelScope.launch {
            runCatching {
                repository.markAsRead(id)
            }.onSuccess {
                loadNotifications()
            }.onFailure {
                // ignore failure for mark as read
            }
        }
    }

    private fun markAllAsRead(pacienteId: Long?) {
        viewModelScope.launch {
            runCatching {
                repository.markAllAsRead(pacienteId ?: sessionManager.getPacienteId())
            }.onFailure {
                // ignore failure
            }
        }
    }
}