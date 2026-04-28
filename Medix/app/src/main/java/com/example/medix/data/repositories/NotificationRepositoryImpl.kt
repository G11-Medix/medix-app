package com.example.medix.data.repositories


import com.example.medix.data.sources.local.dao.NotificationDao
import com.example.medix.data.sources.local.entity.NotificationEntity
import com.example.medix.domain.entities.Notification
import com.example.medix.domain.repositories.NotificationRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val dao: NotificationDao
) : NotificationRepository {

    override suspend fun getNotifications(): List<Notification> {

        val oneDayAgo = System.currentTimeMillis() - (24 * 60 * 60 * 1000)

        dao.deleteOld(oneDayAgo)

        return dao.getAll().map {
            Notification(
                title = it.title,
                body = it.body,
                timestamp = it.timestamp
            )
        }
    }

    override suspend fun saveNotification(notification: Notification) {
        dao.insert(
            NotificationEntity(
                title = notification.title,
                body = notification.body,
                timestamp = notification.timestamp
            )
        )
    }
}