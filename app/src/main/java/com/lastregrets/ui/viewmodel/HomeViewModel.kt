package com.lastregrets.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lastregrets.data.model.Regret
import com.lastregrets.data.repository.RegretRepository
import com.lastregrets.data.repository.TodoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val dailyRegret: Regret? = null,
    val isLoading: Boolean = true,
    val totalRegrets: Int = 0,
    val showAddedToast: Boolean = false
)

class HomeViewModel(
    private val regretRepository: RegretRepository,
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDailyRegret()
        observeTotalCount()
    }

    private fun loadDailyRegret() {
        viewModelScope.launch {
            val regret = regretRepository.getRandomRegret()
            _uiState.update {
                it.copy(dailyRegret = regret, isLoading = false)
            }
        }
    }

    private fun observeTotalCount() {
        viewModelScope.launch {
            regretRepository.getTotalCount().collect { count ->
                _uiState.update { it.copy(totalRegrets = count) }
            }
        }
    }

    fun refreshDailyRegret() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val regret = regretRepository.getRandomRegret()
            _uiState.update {
                it.copy(dailyRegret = regret, isLoading = false)
            }
        }
    }

    fun resonateWithRegret() {
        val regret = _uiState.value.dailyRegret ?: return
        viewModelScope.launch {
            regretRepository.resonate(regret)
            // 更新本地 UI 显示（乐观更新）
            _uiState.update {
                it.copy(dailyRegret = regret.copy(resonateCount = regret.resonateCount + 1))
            }
        }
    }

    fun addToTodo(regret: Regret) {
        viewModelScope.launch {
            val result = todoRepository.addRegretToTodo(regret)
            _uiState.update { it.copy(showAddedToast = result > 0) }
        }
    }

    fun dismissToast() {
        _uiState.update { it.copy(showAddedToast = false) }
    }

    companion object {
        fun factory(regretRepository: RegretRepository, todoRepository: TodoRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(regretRepository, todoRepository) as T
                }
            }
        }
    }
}
