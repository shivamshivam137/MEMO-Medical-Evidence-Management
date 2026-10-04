package com.memo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.memo.app.ui.components.ClinicalSearchBar
import com.memo.app.ui.components.EmptyState
import com.memo.app.ui.components.ReportCard
import com.memo.app.ui.theme.*
import com.memo.app.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
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
                text = "Clinical Search",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )

            Spacer(modifier = Modifier.height(10.dp))

            ClinicalSearchBar(
                query = state.query,
                onQueryChange = { viewModel.onQueryChange(it) },
                onClear = { viewModel.clearQuery() },
                placeholder = "Search 'HbA1c', 'Lipid', 'NovaCare', doctor..."
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Search suggestions chips
            Text(
                text = "Quick Searches",
                style = MaterialTheme.typography.labelSmall,
                color = Slate500
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.suggestions) { suggestion ->
                    Surface(
                        color = SurfaceWhite,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
                        modifier = Modifier.clickable { viewModel.applySuggestion(suggestion) }
                    ) {
                        Text(
                            text = suggestion,
                            style = MaterialTheme.typography.bodySmall,
                            color = Teal700,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        if (state.query.isBlank()) {
            EmptyState(message = "Type a biomarker, physician name, or diagnostic facility above.")
        } else if (state.results.isEmpty()) {
            EmptyState(message = "No clinical records found matching \"${state.query}\"")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Found ${state.results.size} matching record(s)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                }

                items(state.results) { report ->
                    ReportCard(
                        report = report,
                        onClick = { onNavigateToReport(report.id) }
                    )
                }
            }
        }
    }
}
