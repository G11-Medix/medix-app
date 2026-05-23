package com.example.medix.core.network

import com.example.medix.data.dto.ConversationRequest
import com.example.medix.data.dto.ConversationResponse
import com.example.medix.data.dto.TranscriptionResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @POST("/conversation")
    suspend fun sendMessage(
        @Body request: ConversationRequest,
    ): ConversationResponse

    @POST("/chat/conversation")
    suspend fun sendChatMessage(
        @Body request: ConversationRequest,
    ): ConversationResponse

    @Multipart
    @POST("/asr/transcribe")
    suspend fun transcribeAudio(
        @Part audio: MultipartBody.Part,
    ): TranscriptionResponse
}
