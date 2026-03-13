package com.lastregrets.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lastregrets.data.model.TodoItem
import com.lastregrets.data.repository.TodoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TodoUiState(
    val activeTodos: List<TodoItem> = emptyList(),
    val completedTodos: List<TodoItem> = emptyList(),
    val showCompleted: Boolean = false,
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val isLoading: Boolean = true
)

class TodoViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    init {
        observeTodos()
        observeStats()
    }

    private fun observeTodos() {
        viewModelScope.launch {
            todoRepository.getActiveTodos().collect { todos ->
                _uiState.update { it.copy(activeTodos = todos, isLoading = false) }
            }
        }
        viewModelScope.launch {
            todoRepository.getCompletedTodos().collect { todos ->
                _uiState.update { it.copy(completedTodos = todos) }
            }
        }
    }

    private fun observeStats() {
        viewModelScope.launch {
            todoRepository.getCompletedCount().collect { count ->
                _uiState.update { it.copy(completedCount = count) }
            }
        }
        viewModelScope.launch {
            todoRepository.getTotalTodoCount().collect { count ->
                _uiState.update { it.copy(totalCount = count) }
            }
        }
    }

    fun toggleShowCompleted() {
        _uiState.update { it.copy(showCompleted = !it.showCompleted) }
    }

    fun completeTodo(todoId: Long) {
        viewModelScope.launch {
            todoRepository.completeTodo(todoId)
        }
    }

    fun uncompleteTodo(todoId: Long) {
        viewModelScope.launch {
            todoRepository.uncompleteTodo(todoId)
        }
    }

    fun deleteTodo(todoId: Long) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todoId)
        }
    }

    fun updateActionPlan(todoItem: TodoItem, newPlan: String) {
        viewModelScope.launch {
            todoRepository.updateActionPlan(todoItem, newPlan)
        }
    }

    companion object {
        fun factory(todoRepository: TodoRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TodoViewModel(todoRepository) as T
                }
            }
        }
    }
}
