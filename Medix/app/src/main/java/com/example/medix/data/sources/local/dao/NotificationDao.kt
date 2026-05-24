package com.example.medix.data.sources.local.dao

import androidx.room.*
import com.example.medix.data.sources.local.entity.NotificationEntity

@Suppress("unused")
@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity): Long

    @Query("SELECT * FROM notifications WHERE pacienteId = :pacienteId ORDER BY timestamp DESC")
    suspend fun getByPacienteId(pacienteId: Long): List<NotificationEntity>

    @Query("DELETE FROM notifications WHERE timestamp < :limit AND pacienteId = :pacienteId")
    suspend fun deleteOld(limit: Long, pacienteId: Long): Int

    @Query("DELETE FROM notifications")
    suspend fun deleteAll(): Int

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteById(id: Int): Int

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Int): Int

    @Query("UPDATE notifications SET isRead = 1 WHERE pacienteId = :pacienteId")
    suspend fun markAllAsRead(pacienteId: Long): Int
}