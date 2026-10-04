package com.memo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.memo.app.ui.components.CategoryFilterRow
import com.memo.app.ui.components.ClinicalSearchBar
import com.memo.app.ui.components.EmptyState
import com.memo.app.ui.components.ReportCard
import com.memo.app.ui.theme.BackgroundLight
import com.memo.app.ui.theme.Slate500
import com.memo.app.ui.theme.Slate900
import com.memo.app.viewmodel.ReportsViewModel

@Composable
fun ReportsListScreen(
    viewModel: ReportsViewModel,
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
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Medical Reports",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )

            Spacer(modifier = Modifier.height(10.dp))

            ClinicalSearchBar(
                query = state.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) },
                onClear = { viewModel.clearSearch() }
            )
        }

        CategoryFilterRow(
            selectedType = state.selectedType,
            onTypeSelected = { viewModel.selectType(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Showing ${state.reports.size} records",
                style = MaterialTheme.typography.bodySmall,
                color = Slate500
            )
        }

        if (state.reports.isEmpty()) {
            EmptyState(
                message = if (state.searchQuery.isNotBlank()) {
                    "No medical reports match \"${state.searchQuery}\""
                } else {
                    "No records found in category ${state.selectedType.displayName}"
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.reports) { report ->
                    ReportCard(
                        report = report,
                        onClick = { onNavigateToReport(report.id) }
                    )
                }
            }
        }
    }
}
