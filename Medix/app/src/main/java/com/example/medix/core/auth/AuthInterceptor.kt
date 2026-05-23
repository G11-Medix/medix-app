package com.example.medix.core.auth

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val token = runBlocking {
            sessionManager.getToken()
        }

        Log.d("AUTH", "TOKEN = $token")

        val request = chain.request().newBuilder().apply {
            token?.let {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()

        val response = chain.proceed(request)


        if (response.code == 401) {
            Log.d("AUTH", "401 detected → clearing session")

            runBlocking {
                sessionManager.clearSession()
            }
        }

        return response
    }
}