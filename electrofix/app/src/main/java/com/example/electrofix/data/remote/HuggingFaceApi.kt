
package com.example.electrofix.data.remote

import com.squareup.moshi.Json
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// --- Data Classes for API Communication ---

data class ChatRequest(
    val inputs: Inputs
)

data class Inputs(
    @Json(name = "past_user_inputs") val pastUserInputs: List<String>,
    @Json(name = "generated_responses") val generatedResponses: List<String>,
    @Json(name = "text") val text: String
)

// The API returns a list of these objects, so we expect List<ChatResponse>
data class ChatResponse(
    @Json(name = "generated_text") val generatedText: String
)


// --- Retrofit API Interface ---

interface HuggingFaceApi {

    // Using a conversational model like "microsoft/DialoGPT-medium"
    @POST("models/microsoft/DialoGPT-medium")
    suspend fun getChatResponse(
        @Header("Authorization") apiKey: String,
        @Body request: ChatRequest
    ): List<ChatResponse>
}

