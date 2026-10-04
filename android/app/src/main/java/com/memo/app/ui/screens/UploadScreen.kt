package com.memo.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.memo.app.ui.theme.*
import com.memo.app.viewmodel.ProcessingStage
import com.memo.app.viewmodel.UploadViewModel

@Composable
fun UploadScreen(
    viewModel: UploadViewModel,
    onNavigateToReport: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    val sampleFiles = listOf(
        "blood_report_cbc_novacare.pdf",
        "prescription_metro_hospital.jpg",
        "lipid_panel_citymed.pdf",
        "ultrasound_abdomen_summary.pdf"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Ingest Medical Document",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )
            Text(
                text = "Digitize PDFs, lab panels, or prescription scans into your structured medical memory.",
                style = MaterialTheme.typography.bodySmall,
                color = Slate500
            )
        }

        // File Selection Box
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = BorderStroke(1.dp, if (state.selectedFileName != null) Teal600 else Slate200)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Teal100),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = null,
                            tint = Teal700,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (state.selectedFileName != null) {
                            "Ready to Ingest: ${state.selectedFileName}"
                        } else {
                            "Select Medical Document"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Supported formats: PDF, JPG, PNG (Max 15MB)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Demonstration Quick Pickers
                    Text(
                        text = "Or choose a test file for the live demo:",
                        style = MaterialTheme.typography.labelSmall,
                        color = Slate600
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    sampleFiles.forEach { file ->
                        val isChosen = (state.selectedFileName == file)
                        OutlinedButton(
                            onClick = { viewModel.selectFile(file) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isChosen) Teal50 else SurfaceWhite
                            ),
                            border = BorderStroke(1.dp, if (isChosen) Teal600 else Slate200),
                            enabled = !state.isProcessing
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = null,
                                        tint = if (isChosen) Teal700 else Slate500,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = file,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isChosen) Teal700 else Slate700,
                                        fontWeight = if (isChosen) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                                if (isChosen) {
                                    Icon(
                                        imageVector = Icons.Outlined.Check,
                                        contentDescription = "Selected",
                                        tint = Teal600,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Processing Pipeline Animation Card
        if (state.isProcessing || state.stage == ProcessingStage.COMPLETE) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = BorderStroke(1.dp, Slate200)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Simulated Pipeline Execution",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { state.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = Teal600,
                            trackColor = Slate100
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        StepItem(
                            stepName = "1. Sandbox Upload",
                            description = "File received and verified locally",
                            isActive = state.stage == ProcessingStage.UPLOADING,
                            isDone = state.progress >= 0.40f
                        )

                        StepItem(
                            stepName = "2. OCR Processing & Layout Detection",
                            description = "PaddleOCR heuristic pipeline simulation",
                            isActive = state.stage == ProcessingStage.READING,
                            isDone = state.progress >= 0.70f
                        )

                        StepItem(
                            stepName = "3. Clinical Entity & Biomarker Extraction",
                            description = "Reference ranges and abnormal tags classified",
                            isActive = state.stage == ProcessingStage.EXTRACTING,
                            isDone = state.progress >= 0.90f
                        )

                        StepItem(
                            stepName = "4. Timeline Structuring",
                            description = "Record appended to in-memory health memory",
                            isActive = state.stage == ProcessingStage.ORGANIZING,
                            isDone = state.stage == ProcessingStage.COMPLETE
                        )

                        if (state.stage == ProcessingStage.COMPLETE && state.createdReport != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    val rpt = state.createdReport
                                    viewModel.reset()
                                    if (rpt != null) {
                                        onNavigateToReport(rpt.id)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Teal600)
                            ) {
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "View Newly Ingested Record",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SurfaceWhite
                                )
                            }
                        }
                    }
                }
            }
        }

        // Action Button
        if (!state.isProcessing && state.stage != ProcessingStage.COMPLETE) {
            item {
                Button(
                    onClick = { viewModel.startSimulatedProcessing() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Slate900)
                ) {
                    Text(
                        text = "Start Simulated Ingest & Extraction",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SurfaceWhite
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun StepItem(
    stepName: String,
    description: String,
    isActive: Boolean,
    isDone: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isDone -> StatusNormal
                        isActive -> Teal600
                        else -> Slate200
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = SurfaceWhite,
                    modifier = Modifier.size(14.dp)
                )
            } else if (isActive) {
                CircularProgressIndicator(
                    color = SurfaceWhite,
                    modifier = Modifier.size(12.dp),
                    strokeWidth = 2.dp
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = stepName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isActive || isDone) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isActive || isDone) Slate900 else Slate500
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Slate500
            )
        }
    }
}
