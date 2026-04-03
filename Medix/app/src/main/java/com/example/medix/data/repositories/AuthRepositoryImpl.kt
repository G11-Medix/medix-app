package com.example.medix.data.repositories

import android.util.Log
import com.example.medix.domain.repositories.AuthRepository
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.github.jan.supabase.auth.providers.builtin.Phone
import io.github.jan.supabase.SupabaseClient

class AuthRepositoryImpl(
    private val supabaseClient: SupabaseClient?,
) : AuthRepository {

    override suspend fun requestOtp(phone: String) {
        val client = requireConfiguredClient()

        Log.e( "ALERT", phone)

        client.auth.signInWith(OTP) {
            this.phone = "+$phone"
            channel = Phone.Channel.WHATSAPP
            createUser = false
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

    private fun requireConfiguredClient(): SupabaseClient {
        return supabaseClient ?: error(
            "Supabase no está configurado. Define SUPABASE_URL y SUPABASE_ANON_KEY en las propiedades Gradle."
        )
    }
}
