package com.example.medix.core.network

import com.example.medix.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient

object SupabaseProvider {

    fun createClientOrNull(): SupabaseClient? {
        val supabaseUrl = BuildConfig.SUPABASE_URL
        val supabaseAnonKey = BuildConfig.SUPABASE_ANON_KEY

        if (supabaseUrl.isBlank() || supabaseAnonKey.isBlank()) {
            return null
        }

        return createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseAnonKey,
        ) {
            install(Auth)
        }
    }
}
