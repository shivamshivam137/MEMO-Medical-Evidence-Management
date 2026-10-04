package com.memo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.app.data.model.MedicalReport
import com.memo.app.data.repository.IMemoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class TimelineGroup(
    val period: String, // e.g., "October 2026", "September 2026", "2025 Baseline"
    val reports: List<MedicalReport>
)

data class TimelineUiState(
    val groups: List<TimelineGroup> = emptyList(),
    val isLoading: Boolean = true
)

class TimelineViewModel(
    private val repository: IMemoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimelineUiState())
    val uiState = _uiState.asStateFlow()

    init {
        repository.getReports().onEach { reports ->
            val grouped = groupReportsChronologically(reports)
            _uiState.value = TimelineUiState(groups = grouped, isLoading = false)
        }.launchIn(viewModelScope)
    }

    private fun groupReportsChronologically(reports: List<MedicalReport>): List<TimelineGroup> {
        // Group by Year-Month
        return reports.groupBy { report ->
            formatPeriod(report.date)
        }.map { (period, list) ->
            TimelineGroup(period = period, reports = list)
        }
    }

    private fun formatPeriod(dateStr: String): String {
        return try {
            val parts = dateStr.split("-")
            if (parts.size >= 2) {
                val year = parts[0]
                val month = when (parts[1]) {
                    "01" -> "January"
                    "02" -> "February"
                    "03" -> "March"
                    "04" -> "April"
                    "05" -> "May"
                    "06" -> "June"
                    "07" -> "July"
                    "08" -> "August"
                    "09" -> "September"
                    "10" -> "October"
                    "11" -> "November"
                    "12" -> "December"
                    else -> parts[1]
                }
                "$month $year"
            } else dateStr
        } catch (e: Exception) {
            dateStr
        }
    }
}
