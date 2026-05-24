package com.example.medix.presentation.viewmodels.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.core.auth.SessionManager
import com.example.medix.domain.repositories.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedNotificationViewModel @Inject constructor(
    private val repository: NotificationRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _notificationCount = MutableStateFlow(0)
    val notificationCount: StateFlow<Int> = _notificationCount

    init {
        viewModelScope.launch {
            sessionManager.sessionFlow.collectLatest { session ->
                updateNotificationCount(session.pacienteId)
            }
        }
    }

    fun updateNotificationCount(pacienteId: Long? = null) {
        viewModelScope.launch {
            runCatching {
                val id = pacienteId ?: sessionManager.getPacienteId()
                val notifications = repository.getNotifications(id)
                // Count only unread notifications
                _notificationCount.value = notifications.count { !it.isRead }
            }.onFailure {
                _notificationCount.value = 0
            }
        }
    }

    fun clearBadge() {
        _notificationCount.value = 0
    }
}


