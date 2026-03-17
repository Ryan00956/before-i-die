package com.lastregrets.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lastregrets.data.local.ContentFilter
import com.lastregrets.data.model.RegretCategory
import com.lastregrets.data.repository.RegretRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PublishUiState(
    val content: String = "",
    val selectedCategory: RegretCategory = RegretCategory.OTHER,
    val isSubmitting: Boolean = false,
    val isSubmitted: Boolean = false,
    val isCloudSynced: Boolean = false,
    val errorMessage: String? = null
)

class PublishViewModel(
    private val regretRepository: RegretRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PublishUiState())
    val uiState: StateFlow<PublishUiState> = _uiState.asStateFlow()

    fun updateContent(content: String) {
        if (content.length <= 200) {
            _uiState.update { it.copy(content = content, errorMessage = null) }
        }
    }

    fun selectCategory(category: RegretCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun submit() {
        val state = _uiState.value
        if (state.content.isBlank()) {
            _uiState.update { it.copy(errorMessage = "请写下一条遗憾") }
            return
        }
        if (state.content.length < 5) {
            _uiState.update { it.copy(errorMessage = "内容太短了，多写几个字吧") }
            return
        }
        // 内容审核
        val filterError = ContentFilter.check(state.content)
        if (filterError != null) {
            _uiState.update { it.copy(errorMessage = filterError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            try {
                val result = regretRepository.submitRegret(state.content, state.selectedCategory)
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        isSubmitted = true,
                        isCloudSynced = result.cloudSuccess
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isSubmitting = false, errorMessage = "提交失败，请重试")
                }
            }
        }
    }

    fun reset() {
        _uiState.value = PublishUiState()
    }

    companion object {
        fun factory(regretRepository: RegretRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PublishViewModel(regretRepository) as T
                }
            }
        }
    }
}
