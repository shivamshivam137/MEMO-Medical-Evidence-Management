package com.memo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.app.data.model.HealthStats
import com.memo.app.data.model.MedicalReport
import com.memo.app.data.model.User
import com.memo.app.data.repository.IMemoRepository
import kotlinx.coroutines.flow.*

data class DashboardUiState(
    val user: User? = null,
    val stats: HealthStats? = null,
    val recentReports: List<MedicalReport> = emptyList(),
    val isLoading: Boolean = true
)

class DashboardViewModel(
    private val repository: IMemoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        combine(
            repository.getCurrentUser(),
            repository.getHealthStats(),
            repository.getReports()
        ) { user, stats, reports ->
            DashboardUiState(
                user = user,
                stats = stats,
                recentReports = reports.take(4),
                isLoading = false
            )
        }.onEach { state ->
            _uiState.value = state
        }.launchIn(viewModelScope)
    }
}
