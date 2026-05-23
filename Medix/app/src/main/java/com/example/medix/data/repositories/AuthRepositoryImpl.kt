package com.example.medix.data.repositories

import com.example.medix.domain.repositories.AuthRepository
import com.example.medix.core.auth.SessionManager
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.github.jan.supabase.auth.providers.builtin.Phone
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.runBlocking

class AuthRepositoryImpl(
    private val supabaseClient: SupabaseClient?,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun requestOtp(
        phone: String,
        createUser: Boolean,
    ) {
        val client = requireConfiguredClient()

        client.auth.signInWith(OTP) {
            this.phone = phone
            channel = Phone.Channel.WHATSAPP
            this.createUser = createUser
        }
    }

    override suspend fun verifyOtp(
        phone: String,
        code: String,
    ): String {
        val client = requireConfiguredClient()

        client.auth.verifyPhoneOtp(
            type = OtpType.Phone.SMS,
            phone = phone,
            token = code,
        )

        return currentUserId()
            ?: error("Supabase autenticó el OTP pero no devolvió un usuario actual.")
    }

    override fun currentUserId(): String? {
        return supabaseClient?.auth?.currentUserOrNull()?.id
    }

    override fun currentAccessToken(): String? {
        return supabaseClient?.auth?.currentAccessTokenOrNull()
    }

    override fun currentRefreshToken(): String? {
        return supabaseClient?.auth?.currentSessionOrNull()?.refreshToken
    }

    override fun clearSessionBlocking() {
        runBlocking {
            sessionManager.clearSession()
        }
    }

    private fun requireConfiguredClient(): SupabaseClient {
        return supabaseClient ?: error(
            "Supabase no está configurado. Define SUPABASE_URL y SUPABASE_ANON_KEY en las propiedades Gradle."
        )
    }
}