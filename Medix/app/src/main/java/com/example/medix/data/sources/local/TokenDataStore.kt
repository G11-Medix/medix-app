package com.example.medix.data.sources.local
import android.content.Context
import android.util.Log

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore by preferencesDataStore(name = "fcm_prefs")

    private object Keys {
        val TOKEN = stringPreferencesKey("fcm_token")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.TOKEN] = token
        }
        Log.d("Dispositivo", "TOKEN_dis = $token")
    }

    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[Keys.TOKEN]
        }


    }
}