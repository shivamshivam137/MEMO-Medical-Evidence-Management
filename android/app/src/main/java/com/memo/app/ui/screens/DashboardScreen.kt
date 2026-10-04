package com.memo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.memo.app.ui.components.EmptyState
import com.memo.app.ui.components.MetricCard
import com.memo.app.ui.components.ReportCard
import com.memo.app.ui.theme.*
import com.memo.app.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToReport: (String) -> Unit,
    onNavigateToReportsList: () -> Unit,
    onNavigateToUpload: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToUpload,
                containerColor = Teal600,
                contentColor = SurfaceWhite,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Upload Report")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Upload Report",
                    style = MaterialTheme.typography.labelLarge,
                    color = SurfaceWhite
                )
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Teal600)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundLight)
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Profile Bar
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Hello, ${state.user?.name ?: "Patient"} 👋",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Text(
                                text = "Patient ID: MEMO-P-88219 • ${state.user?.bloodGroup ?: "B+"} • Age ${state.user?.age ?: 42}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Slate500
                            )
                        }

                        IconButton(
                            onClick = onNavigateToProfile,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Teal100)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = Teal700
                            )
                        }
                    }
                }

                // Health Metrics Grid
                item {
                    val stats = state.stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        MetricCard(
                            count = "${stats?.totalReports ?: 0}",
                            label = "Total Records",
                            icon = Icons.Outlined.Folder,
                            iconTint = Slate700,
                            bgColor = Slate100,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            count = "${stats?.bloodTestsCount ?: 0}",
                            label = "Blood Tests",
                            icon = Icons.Outlined.Bloodtype,
                            iconTint = CategoryBlood,
                            bgColor = CategoryBloodBg,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    val stats = state.stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        MetricCard(
                            count = "${stats?.prescriptionsCount ?: 0}",
                            label = "Prescriptions",
                            icon = Icons.Outlined.Medication,
                            iconTint = CategoryPrescription,
                            bgColor = CategoryPrescriptionBg,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            count = "${stats?.imagingCount ?: 0}",
                            label = "Imaging & Scans",
                            icon = Icons.Outlined.CameraAlt,
                            iconTint = CategoryImaging,
                            bgColor = CategoryImagingBg,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Attention Callout if abnormal values exist
                if ((state.stats?.attentionRequiredCount ?: 0) > 0) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = StatusWarningBg)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = StatusWarning,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "${state.stats?.attentionRequiredCount} reports have values outside standard range",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = StatusWarning
                                    )
                                    Text(
                                        text = "E.g., Elevated lipid & borderline HbA1c tests. Tap reports for detailed lab reference intervals.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Slate700
                                    )
                                }
                            }
                        }
                    }
                }

                // Section Header: Recent Medical Records
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent Records",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate900
                        )

                        TextButton(onClick = onNavigateToReportsList) {
                            Text(
                                text = "View All",
                                style = MaterialTheme.typography.labelLarge,
                                color = Teal600,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Recent Reports List
                if (state.recentReports.isEmpty()) {
                    item {
                        EmptyState(message = "No medical records found yet.")
                    }
                } else {
                    items(state.recentReports) { report ->
                        ReportCard(
                            report = report,
                            onClick = { onNavigateToReport(report.id) }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(72.dp)) // Space for FAB
                }
            }
        }
    }
}
