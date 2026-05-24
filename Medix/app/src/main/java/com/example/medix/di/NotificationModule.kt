package com.example.medix.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.medix.data.sources.local.NotificationDatabase
import com.example.medix.data.sources.local.dao.NotificationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "ALTER TABLE notifications ADD COLUMN pacienteId INTEGER NOT NULL DEFAULT -1"
            )
        }
    }

    @Provides
    @Singleton
    @Suppress("unused")
    fun provideDatabase(
        @ApplicationContext context: Context
    ): NotificationDatabase {
        return Room.databaseBuilder(
            context,
            NotificationDatabase::class.java,
            "medix_db"
        ).addMigrations(MIGRATION_2_3)
            .build()
    }

    @Provides
    @Suppress("unused")
    fun provideNotificationDao(
        db: NotificationDatabase
    ): NotificationDao = db.notificationDao()
}