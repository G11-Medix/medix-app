package com.example.medix.core.network

import com.example.medix.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient

object SupabaseProvider {

    fun createClient(): SupabaseClient {

        val supabaseUrl = BuildConfig.SUPABASE_URL
        val supabaseAnonKey = BuildConfig.SUPABASE_ANON_KEY

        require(supabaseUrl.isNotBlank()) {
            "SUPABASE_URL no está configurado en BuildConfig"
        }

        require(supabaseAnonKey.isNotBlank()) {
            "SUPABASE_ANON_KEY no está configurado en BuildConfig"
        }

        return createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseAnonKey
        ) {
            install(Auth)
        }
    }
}