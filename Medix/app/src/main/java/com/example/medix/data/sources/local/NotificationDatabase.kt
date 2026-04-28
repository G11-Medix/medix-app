package com.example.medix.data.sources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.medix.data.sources.local.dao.NotificationDao
import com.example.medix.data.sources.local.entity.NotificationEntity


@Database(
    entities = [NotificationEntity::class],
    version = 1
)
abstract class NotificationDatabase : RoomDatabase() {

    abstract fun notificationDao(): NotificationDao
}