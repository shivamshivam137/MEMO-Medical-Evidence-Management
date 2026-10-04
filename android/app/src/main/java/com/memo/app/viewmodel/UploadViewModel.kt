package com.memo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.app.data.model.MedicalReport
import com.memo.app.data.repository.IMemoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ProcessingStage(val title: String, val description: String) {
    IDLE("Ready", "Select a medical report to upload"),
    UPLOADING("Uploading Document", "Securing file transmission to local sandbox..."),
    READING("OCR Preprocessing", "Normalizing contrast and detecting document regions..."),
    EXTRACTING("Extracting Biomarkers", "Parsing clinical lab entities, units, and reference ranges..."),
    ORGANIZING("Structuring Timeline", "Categorizing report and updating personal health memory..."),
    COMPLETE("Extraction Complete", "Structured clinical record ready for review!")
}

data class UploadUiState(
    val selectedFileName: String? = null,
    val stage: ProcessingStage = ProcessingStage.IDLE,
    val progress: Float = 0f,
    val createdReport: MedicalReport? = null,
    val isProcessing: Boolean = false,
    val errorMessage: String? = null
)

class UploadViewModel(
    private val repository: IMemoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UploadUiState())
    val uiState = _uiState.asStateFlow()

    fun selectFile(fileName: String) {
        _uiState.value = UploadUiState(
            selectedFileName = fileName,
            stage = ProcessingStage.IDLE,
            progress = 0f
        )
    }

    fun startSimulatedProcessing() {
        val fileName = _uiState.value.selectedFileName ?: "blood_report_cbc_novacare.pdf"
        _uiState.value = _uiState.value.copy(
            isProcessing = true,
            stage = ProcessingStage.UPLOADING,
            progress = 0.15f
        )

        viewModelScope.launch {
            // Stage 1: Uploading
            delay(700)
            _uiState.value = _uiState.value.copy(
                stage = ProcessingStage.READING,
                progress = 0.40f
            )

            // Stage 2: OCR Preprocessing & Heuristics
            delay(900)
            _uiState.value = _uiState.value.copy(
                stage = ProcessingStage.EXTRACTING,
                progress = 0.70f
            )

            // Stage 3: Information Extraction
            delay(1000)
            _uiState.value = _uiState.value.copy(
                stage = ProcessingStage.ORGANIZING,
                progress = 0.90f
            )

            // Final: Complete and insert
            delay(600)
            val result = repository.uploadReport(fileName)
            result.fold(
                onSuccess = { report ->
                    _uiState.value = _uiState.value.copy(
                        isProcessing = false,
                        stage = ProcessingStage.COMPLETE,
                        progress = 1.0f,
                        createdReport = report
                    )
                },
                onFailure = { err ->
                    _uiState.value = _uiState.value.copy(
                        isProcessing = false,
                        errorMessage = err.message ?: "Processing error"
                    )
                }
            )
        }
    }

    fun reset() {
        _uiState.value = UploadUiState()
    }
}
