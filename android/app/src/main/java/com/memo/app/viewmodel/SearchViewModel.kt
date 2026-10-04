package com.memo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.app.data.model.MedicalReport
import com.memo.app.data.model.ReportType
import com.memo.app.data.repository.IMemoRepository
import kotlinx.coroutines.flow.*

data class SearchUiState(
    val query: String = "",
    val results: List<MedicalReport> = emptyList(),
    val suggestions: List<String> = listOf("HbA1c", "Lipid Profile", "NovaCare", "Urine", "Dr. Priya", "Cardiology"),
    val isSearching: Boolean = false
)

class SearchViewModel(
    private val repository: IMemoRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _query.debounce(250)
            .flatMapLatest { q ->
                if (q.isBlank()) {
                    flowOf(emptyList())
                } else {
                    repository.getFilteredReports(ReportType.ALL, q)
                }
            }.onEach { matches ->
                _uiState.value = _uiState.value.copy(
                    query = _query.value,
                    results = matches,
                    isSearching = false
                )
            }.launchIn(viewModelScope)
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        _uiState.value = _uiState.value.copy(
            query = newQuery,
            isSearching = newQuery.isNotBlank()
        )
    }

    fun applySuggestion(suggestion: String) {
        onQueryChange(suggestion)
    }

    fun clearQuery() {
        onQueryChange("")
    }
}
