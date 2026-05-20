package com.example.medix.di

import android.content.Context
import androidx.room.Room
import com.example.medix.data.sources.local.NotificationDatabase
import com.example.medix.data.sources.local.dao.NotificationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): NotificationDatabase {
        return Room.databaseBuilder(
            context,
            NotificationDatabase::class.java,
            "medix_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideNotificationDao(
        db: NotificationDatabase
    ): NotificationDao = db.notificationDao()
}