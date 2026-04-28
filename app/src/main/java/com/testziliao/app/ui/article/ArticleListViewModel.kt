package com.testziliao.app.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.testziliao.app.data.repository.ContentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ArticleListViewModel(
    contentRepository: ContentRepository,
    private val categoryId: String,
    private val categoryName: String
) : ViewModel() {

    val uiState: StateFlow<ArticleListUiState> = contentRepository.observeArticlesByCategory(categoryId)
        .map {
            ArticleListUiState(
                articles = it,
                categoryName = categoryName
            )
        }
        .catch {
            emit(
                ArticleListUiState(
                    categoryName = categoryName,
                    message = it.message ?: "文章列表加载失败"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ArticleListUiState(categoryName = categoryName)
        )

    companion object {
        fun factory(
            contentRepository: ContentRepository,
            categoryId: String,
            categoryName: String
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ArticleListViewModel(
                        contentRepository = contentRepository,
                        categoryId = categoryId,
                        categoryName = categoryName
                    ) as T
                }
            }
        }
    }
}
