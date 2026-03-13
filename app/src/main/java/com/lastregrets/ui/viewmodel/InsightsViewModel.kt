package com.lastregrets.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lastregrets.data.local.CategoryStat
import com.lastregrets.data.model.Regret
import com.lastregrets.data.repository.RegretRepository
import com.lastregrets.data.repository.TodoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class InsightsUiState(
    val topRegrets: List<Regret> = emptyList(),
    val categoryStats: List<CategoryStat> = emptyList(),
    val totalRegrets: Int = 0,
    val completedTodos: Int = 0,
    val totalTodos: Int = 0,
    val isLoading: Boolean = true
)

class InsightsViewModel(
    private val regretRepository: RegretRepository,
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsightsUiState())
    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            regretRepository.getTopRegrets(10).collect { top ->
                _uiState.update { it.copy(topRegrets = top, isLoading = false) }
            }
        }
        viewModelScope.launch {
            regretRepository.getCategoryStats().collect { stats ->
                _uiState.update { it.copy(categoryStats = stats) }
            }
        }
        viewModelScope.launch {
            regretRepository.getTotalCount().collect { count ->
                _uiState.update { it.copy(totalRegrets = count) }
            }
        }
        viewModelScope.launch {
            todoRepository.getCompletedCount().collect { count ->
                _uiState.update { it.copy(completedTodos = count) }
            }
        }
        viewModelScope.launch {
            todoRepository.getTotalTodoCount().collect { count ->
                _uiState.update { it.copy(totalTodos = count) }
            }
        }
    }

    companion object {
        fun factory(regretRepository: RegretRepository, todoRepository: TodoRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return InsightsViewModel(regretRepository, todoRepository) as T
                }
            }
        }
    }
}
