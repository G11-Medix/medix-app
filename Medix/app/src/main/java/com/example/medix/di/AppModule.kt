package com.example.medix.di

import com.example.medix.BuildConfig
import com.example.medix.core.auth.*
import com.example.medix.core.network.*
import com.example.medix.core.network.SupabaseProvider
import com.example.medix.data.repositories.*
import com.example.medix.domain.repositories.*
import com.example.medix.services.AudioPlayer
import com.example.medix.services.AudioRecorder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // =========================
    // SUPABASE
    // =========================
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return SupabaseProvider.createClient()
    }

    // =========================
    // SESSION MANAGER
    // =========================
    @Provides
    @Singleton
    fun provideSessionManager(
        dataStore: androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences>
    ): SessionManager {
        return SessionManager(dataStore)
    }


    // =========================
    // AUTH INTERCEPTOR
    // =========================
    @Provides
    @Singleton
    fun provideAuthInterceptor(
        sessionManager: SessionManager
    ): AuthInterceptor {
        return AuthInterceptor(sessionManager)
    }



    // =========================
    // OKHTTP CLIENT
    // =========================
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        
    ): OkHttpClient {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    // =========================
    // RETROFIT
    // =========================
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.MEDIX_API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // =========================
    // AUTH REPOSITORY
    // =========================
    @Provides
    @Singleton
    fun provideAuthRepository(
        supabaseClient: SupabaseClient?,
        sessionManager: SessionManager
    ): AuthRepository {
        return AuthRepositoryImpl(
            supabaseClient = supabaseClient,
            sessionManager = sessionManager
        )
    }

    // =========================
    // APIs
    // =========================
    @Provides
    @Singleton
    fun provideAppointmentApi(retrofit: Retrofit): AppointmentApi =
        retrofit.create(AppointmentApi::class.java)

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi =
        retrofit.create(ProfileApi::class.java)

    @Provides
    @Singleton
    fun provideConfirmationApi(retrofit: Retrofit): ConfirmationApi =
        retrofit.create(ConfirmationApi::class.java)

    @Provides
    @Singleton
    fun providePacienteApi(retrofit: Retrofit): PacienteApiService =
        retrofit.create(PacienteApiService::class.java)

    @Provides
    @Singleton
    fun provideVoiceApi(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    // =========================
    // REPOSITORIES
    // =========================
    @Provides
    @Singleton
    fun provideAppointmentRepository(
        api: AppointmentApi,
        sessionManager: SessionManager
    ): AppointmentRepository =
        AppointmentRepositoryImpl(api, sessionManager)

    @Provides
    @Singleton
    fun provideProfileRepository(
        api: ProfileApi,
        sessionManager: SessionManager
    ): ProfileRepository =
        ProfileRepositoryImpl(api, sessionManager)

    @Provides
    @Singleton
    fun provideConfirmationRepository(api: ConfirmationApi): ConfirmationRepository =
        ConfirmationRepositoryImpl(api)

    @Provides
    @Singleton
    fun providePacienteRepository(api: PacienteApiService): PacienteRepository =
        PacienteRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideVoiceRepository(
        api: ApiService,
        webSocketClient: WebSocketClient
    ): VoiceRepository =
        VoiceRepositoryImpl(api, webSocketClient)

    // =========================
    // AUDIO
    // =========================
    @Provides
    @Singleton
    fun provideAudioRecorder(
        @ApplicationContext context: android.content.Context
    ): AudioRecorder = AudioRecorder(context)

    @Provides
    @Singleton
    fun provideAudioPlayer(
        @ApplicationContext context: android.content.Context
    ): AudioPlayer = AudioPlayer(context)
}