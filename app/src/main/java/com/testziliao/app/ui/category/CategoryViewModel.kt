package com.testziliao.app.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.testziliao.app.data.repository.ContentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CategoryViewModel(
    contentRepository: ContentRepository
) : ViewModel() {

    val uiState: StateFlow<CategoryUiState> = contentRepository.observeCategories()
        .map { CategoryUiState(categories = it) }
        .catch { emit(CategoryUiState(message = it.message ?: "分类加载失败")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CategoryUiState()
        )

    companion object {
        fun factory(contentRepository: ContentRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CategoryViewModel(contentRepository) as T
                }
            }
        }
    }
}
