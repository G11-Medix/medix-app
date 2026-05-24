package com.example.medix.data.sources.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pacienteId: Long = -1,
    val title: String,
    val body: String,
    val timestamp: Long,
    val isRead: Boolean = false
)