package com.example.medix.data.sources.local.dao

import androidx.room.*
import com.example.medix.data.sources.local.entity.NotificationEntity

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity): Long

    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    suspend fun getAll(): List<NotificationEntity>

    @Query("DELETE FROM notifications WHERE timestamp < :limit")
    suspend fun deleteOld(limit: Long): Int
}