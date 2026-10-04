package com.memo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.memo.app.ui.components.ReportCard
import com.memo.app.ui.theme.*
import com.memo.app.viewmodel.TimelineViewModel

@Composable
fun TimelineScreen(
    viewModel: TimelineViewModel,
    onNavigateToReport: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Longitudinal Timeline",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )
            Text(
                text = "Chronological progression of personal medical evidence",
                style = MaterialTheme.typography.bodySmall,
                color = Slate500
            )
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Teal600)
            }
        } else if (state.groups.isEmpty()) {
            EmptyState(message = "No chronological records available.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                state.groups.forEach { group ->
                    // Period Sticky Header
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Teal600)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = group.period,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Slate800
                            )
                        }
                    }

                    items(group.reports) { report ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Vertical timeline bar
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(24.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Slate300)
                                )
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .weight(1f, fill = false)
                                        .height(100.dp)
                                        .background(Slate200)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                ReportCard(
                                    report = report,
                                    onClick = { onNavigateToReport(report.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
