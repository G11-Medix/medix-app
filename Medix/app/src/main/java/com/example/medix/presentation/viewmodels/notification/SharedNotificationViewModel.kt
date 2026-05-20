package com.example.medix.presentation.viewmodels.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.domain.repositories.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedNotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
) : ViewModel() {

    private val _notificationCount = MutableStateFlow(0)
    val notificationCount: StateFlow<Int> = _notificationCount

    init {
        updateNotificationCount()
    }

    fun updateNotificationCount() {
        viewModelScope.launch {
            runCatching {
                val notifications = repository.getNotifications()
                // Count only unread notifications
                _notificationCount.value = notifications.count { !it.isRead }
            }.onFailure {
                _notificationCount.value = 0
            }
        }
    }
}


