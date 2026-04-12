package com.example.medix.core.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private object Keys {
        val TOKEN = stringPreferencesKey("token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val PACIENTE_ID = longPreferencesKey("paciente_id")
    }


    val sessionFlow: Flow<AuthSessionState> = dataStore.data.map { prefs ->
        AuthSessionState(
            token = prefs[Keys.TOKEN],
            refreshToken = prefs[Keys.REFRESH_TOKEN],
            pacienteId = prefs[Keys.PACIENTE_ID]
        )
    }


    suspend fun saveSession(
        token: String,
        refreshToken: String?,
        pacienteId: Long?
    ) {
        dataStore.edit { prefs ->
            prefs[Keys.TOKEN] = token
            if (refreshToken != null) prefs[Keys.REFRESH_TOKEN] = refreshToken
            if (pacienteId != null) prefs[Keys.PACIENTE_ID] = pacienteId
        }
    }


    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }


    suspend fun getToken(): String? {
        return dataStore.data.map { it[Keys.TOKEN] }.firstOrNull()
    }

    suspend fun getPacienteId(): Long? {
        return dataStore.data.map { it[Keys.PACIENTE_ID] }.firstOrNull()
    }

    suspend fun requirePacienteId(): Long {
        return getPacienteId()
            ?: throw IllegalStateException("Paciente ID no disponible")
    }
}