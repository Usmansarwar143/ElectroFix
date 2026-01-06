
package com.example.electrofix.data.repository

import android.graphics.Bitmap
import com.example.electrofix.viewmodels.DiagnosticResult
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.squareup.moshi.Moshi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiRepository @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val moshi: Moshi
) {

    suspend fun getDiagnostic(
        image: Bitmap,
        userText: String
    ): Result<DiagnosticResult> {
        return try {
            // The specific prompt for the AI model
            val prompt = """You are an expert appliance and electronics repair technician. Analyze this image of a broken item. Respond ONLY with a valid JSON object in this format: { "problem": "Brief description of the issue.", "recommendation": "Service Recommended: [Appliance Repair, General Electrical, Electronics Repair, Smart Home Setup]", "confidence": "XX%", "disclaimer": "This is an AI-generated diagnosis. A professional technician is recommended." }. User's description: $userText"""

            val inputContent = content {
                image(image)
                text(prompt)
            }

            val response = generativeModel.generateContent(inputContent)

            // Clean the response to ensure it's valid JSON
            val jsonResponse = response.text
                ?.trim()
                ?.removePrefix("```json")
                ?.removeSuffix("```")

            if (jsonResponse.isNullOrBlank()) {
                return Result.failure(Exception("Received an empty response from the AI."))
            }

            // Parse the JSON response using Moshi
            val adapter = moshi.adapter(DiagnosticResult::class.java)
            val result = adapter.fromJson(jsonResponse)

            if (result != null) {
                Result.success(result)
            } else {
                Result.failure(Exception("Failed to parse the AI's response."))
            }
        } catch (e: Exception) {
            // Return a failure result with the exception
            Result.failure(e)
        }
    }
}
