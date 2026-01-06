
package com.example.electrofix.data.repository

import com.example.electrofix.BuildConfig
import com.example.electrofix.data.remote.ChatRequest
import com.example.electrofix.data.remote.HuggingFaceApi
import com.example.electrofix.data.remote.Inputs
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HuggingFaceRepository @Inject constructor(
    private val huggingFaceApi: HuggingFaceApi
) {

    suspend fun getChatResponse(
        userInput: String,
        pastUserInputs: List<String>,
        generatedResponses: List<String>
    ): Result<String> {
        return try {
            val request = ChatRequest(
                inputs = Inputs(
                    pastUserInputs = pastUserInputs,
                    generatedResponses = generatedResponses,
                    text = userInput
                )
            )

            // Note the "Bearer " prefix is important for Hugging Face API calls
            val apiKey = "Bearer ${BuildConfig.HF_API_KEY}"

            val response = huggingFaceApi.getChatResponse(apiKey, request)

            // The API returns a list, we take the first generated text if available
            val generatedText = response.firstOrNull()?.generatedText

            if (generatedText != null) {
                Result.success(generatedText)
            } else {
                Result.failure(Exception("Received an empty or invalid response from the AI."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
