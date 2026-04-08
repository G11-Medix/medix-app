package com.example.medix.core.auth

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {
    private const val PREFS_NAME = "medix_session"
    private const val KEY_TOKEN = "token"

    private var prefs: SharedPreferences? = null

    private var pacienteId: Long? = null
    private val _sessionState = MutableStateFlow(AuthSessionState())
    val sessionState: StateFlow<AuthSessionState> = _sessionState.asStateFlow()

    fun initialize(context: Context) {
        if (prefs != null) return

        prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val storedToken = prefs?.getString(KEY_TOKEN, null)

        _sessionState.value = AuthSessionState(
            token = storedToken,
        )
    }

    fun saveSession(token: String) {
        _sessionState.value = AuthSessionState(
            token = token,
        )

        prefs?.edit()
            ?.putString(KEY_TOKEN, token)
            ?.apply()
    }

    fun clearSession() {
        _sessionState.value = AuthSessionState()
        prefs?.edit()
            ?.remove(KEY_TOKEN)
            ?.apply()
    }

    fun isLoggedIn(): Boolean = _sessionState.value.isLoggedIn

    fun getToken(): String? = _sessionState.value.token

    fun requireToken(): String {
        return getToken() ?: error("No hay un token de sesion disponible.")
    }

    fun savePacienteId(id: Long) {
        pacienteId = id
    }

    fun getPacienteId(): Long? = pacienteId

    fun getPacienteIdOrThrow(): Long {
        return pacienteId ?: throw IllegalStateException("Paciente no logueado")
    }
}
