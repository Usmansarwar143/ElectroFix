
package com.example.electrofix.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.electrofix.data.repository.GeminiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Represents the structured data we expect from the Gemini API
data class DiagnosticResult(
    val problem: String = "",
    val recommendation: String = "",
    val confidence: String = "",
    val disclaimer: String = ""
)

// Represents the complete state for the AI Diagnostic screen
data class DiagnosticUiState(
    val selectedImageUri: Uri? = null,
    val selectedImageBitmap: Bitmap? = null,
    val problemDescription: String = "",
    val isLoading: Boolean = false,
    val result: DiagnosticResult? = null,
    val error: String? = null
)

@HiltViewModel
class AIDiagnosticViewModel @Inject constructor(
    private val application: Application,
    private val geminiRepository: GeminiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiagnosticUiState())
    val uiState = _uiState.asStateFlow()

    fun onImageSelected(uri: Uri?) {
        val bitmap = uri?.let { uriToBitmap(it) }
        _uiState.update { it.copy(selectedImageUri = uri, selectedImageBitmap = bitmap) }
    }

    fun onProblemDescriptionChanged(text: String) {
        _uiState.update { it.copy(problemDescription = text) }
    }

    fun runDiagnostic() {
        val currentState = _uiState.value
        val imageBitmap = currentState.selectedImageBitmap

        if (imageBitmap == null || currentState.problemDescription.isBlank()) {
            _uiState.update { it.copy(error = "Please select an image and provide a description.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, result = null) }

            val result = geminiRepository.getDiagnostic(imageBitmap, currentState.problemDescription)

            result.fold(
                onSuccess = {
                    _uiState.update { state -> state.copy(isLoading = false, result = it) }
                },
                onFailure = {
                    _uiState.update { state -> state.copy(isLoading = false, error = it.message ?: "An unknown error occurred.") }
                }
            )
        }
    }

    private fun uriToBitmap(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(application.contentResolver, uri))
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(application.contentResolver, uri)
        }
    }
}
