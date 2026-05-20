package com.example.medix.domain.entities

data class Notification(
    val id: Int = 0,
    val title: String,
    val body: String,
    val timestamp: Long,
    val isRead: Boolean = false
)