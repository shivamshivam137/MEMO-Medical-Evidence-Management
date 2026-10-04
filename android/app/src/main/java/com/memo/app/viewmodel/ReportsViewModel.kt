package com.memo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.app.data.model.MedicalReport
import com.memo.app.data.model.ReportType
import com.memo.app.data.repository.IMemoRepository
import kotlinx.coroutines.flow.*

data class ReportsUiState(
    val reports: List<MedicalReport> = emptyList(),
    val selectedType: ReportType = ReportType.ALL,
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

class ReportsViewModel(
    private val repository: IMemoRepository
) : ViewModel() {

    private val _selectedType = MutableStateFlow(ReportType.ALL)
    private val _searchQuery = MutableStateFlow("")

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        combine(_selectedType, _searchQuery) { type, query ->
            Pair(type, query)
        }.flatMapLatest { (type, query) ->
            repository.getFilteredReports(type, query)
        }.onEach { filteredList ->
            _uiState.value = _uiState.value.copy(
                reports = filteredList,
                selectedType = _selectedType.value,
                searchQuery = _searchQuery.value,
                isLoading = false
            )
        }.launchIn(viewModelScope)
    }

    fun selectType(type: ReportType) {
        _selectedType.value = type
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    fun getReportById(id: String): Flow<MedicalReport?> {
        return repository.getReportById(id)
    }
}
