package com.lastregrets.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lastregrets.data.model.Regret
import com.lastregrets.data.model.RegretCategory
import com.lastregrets.data.repository.RegretRepository
import com.lastregrets.data.repository.TodoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class RegretSquareUiState(
    val regrets: List<Regret> = emptyList(),
    val selectedCategory: RegretCategory? = null,
    val isLoading: Boolean = true,
    val toastMessage: String? = null
)

class RegretSquareViewModel(
    private val regretRepository: RegretRepository,
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegretSquareUiState())
    val uiState: StateFlow<RegretSquareUiState> = _uiState.asStateFlow()

    init {
        loadAllRegrets()
    }

    private fun loadAllRegrets() {
        viewModelScope.launch {
            regretRepository.getAllRegrets().collect { regrets ->
                _uiState.update {
                    it.copy(regrets = regrets, isLoading = false)
                }
            }
        }
    }

    fun selectCategory(category: RegretCategory?) {
        _uiState.update { it.copy(selectedCategory = category, isLoading = true) }
        viewModelScope.launch {
            if (category == null) {
                regretRepository.getAllRegrets().collect { regrets ->
                    _uiState.update { it.copy(regrets = regrets, isLoading = false) }
                }
            } else {
                regretRepository.getRegretsByCategory(category).collect { regrets ->
                    _uiState.update { it.copy(regrets = regrets, isLoading = false) }
                }
            }
        }
    }

    fun resonate(regret: Regret) {
        viewModelScope.launch {
            regretRepository.resonate(regret)
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
