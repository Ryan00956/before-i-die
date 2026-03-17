package com.lastregrets.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lastregrets.data.model.Regret
import com.lastregrets.data.model.RegretCategory
import com.lastregrets.data.repository.RegretRepository
import com.lastregrets.data.repository.TodoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class RegretSquareUiState(
    val regrets: List<Regret> = emptyList(),
    val selectedCategory: RegretCategory? = null,
    val isLoading: Boolean = true,
    val toastMessage: String? = null,
    val resonatedIds: Set<String> = emptySet()
)

class RegretSquareViewModel(
    private val regretRepository: RegretRepository,
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegretSquareUiState())
    val uiState: StateFlow<RegretSquareUiState> = _uiState.asStateFlow()

    private var categoryJob: Job? = null

    init {
        loadRegrets(null)
    }

    private fun loadRegrets(category: RegretCategory?) {
        categoryJob?.cancel()
        categoryJob = viewModelScope.launch {
            val flow = if (category == null) {
                regretRepository.getAllRegrets()
            } else {
                regretRepository.getRegretsByCategory(category)
            }
            flow.collect { regrets ->
                _uiState.update { it.copy(regrets = regrets, isLoading = false) }
            }
        }
    }

    fun selectCategory(category: RegretCategory?) {
        _uiState.update { it.copy(selectedCategory = category, isLoading = true) }
        loadRegrets(category)
    }

    fun resonate(regret: Regret) {
        val identifier = regret.firestoreId ?: regret.id.toString()
        // 防止重复共鸣
        if (identifier in _uiState.value.resonatedIds) return

        viewModelScope.launch {
            regretRepository.resonate(regret)
            _uiState.update {
                it.copy(resonatedIds = it.resonatedIds + identifier)
            }
        }
    }

    fun addToTodo(regret: Regret) {
        viewModelScope.launch {
            val result = todoRepository.addRegretToTodo(regret)
            val message = if (result > 0) "已加入你的待办清单 ✓" else "已在待办清单中"
            _uiState.update { it.copy(toastMessage = message) }
        }
    }

    fun dismissToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    companion object {
        fun factory(regretRepository: RegretRepository, todoRepository: TodoRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RegretSquareViewModel(regretRepository, todoRepository) as T
                }
            }
        }
    }
}
