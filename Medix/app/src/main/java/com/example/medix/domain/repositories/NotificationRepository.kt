package com.example.medix.domain.repositories

import com.example.medix.domain.entities.Notification

interface NotificationRepository {
    suspend fun getNotifications(pacienteId: Long?): List<Notification>
    suspend fun saveNotification(notification: Notification, pacienteId: Long?)
    suspend fun deleteNotification(id: Int)
    suspend fun markAsRead(id: Int)
    suspend fun markAllAsRead(pacienteId: Long?)
}
