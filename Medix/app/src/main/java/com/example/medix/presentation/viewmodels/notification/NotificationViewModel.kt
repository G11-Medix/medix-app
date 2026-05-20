package com.example.medix.presentation.viewmodels.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

import com.example.medix.domain.entities.Notification
import com.example.medix.domain.repositories.NotificationRepository
import com.example.medix.presentation.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Notification>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Notification>>> = _uiState

    init {
        loadNotifications()
        markAllAsRead()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            runCatching {
                repository.getNotifications()
            }.onSuccess { data ->
                _uiState.value = UiState.Success(data)
            }.onFailure { e ->
                _uiState.value = UiState.Error(
                    e.message ?: "Error al cargar notificaciones"
                )
            }
        }
    }

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

    private fun markAllAsRead() {
        viewModelScope.launch {
            runCatching {
                repository.markAllAsRead()
            }.onFailure {
                // ignore failure
            }
        }
    }
}