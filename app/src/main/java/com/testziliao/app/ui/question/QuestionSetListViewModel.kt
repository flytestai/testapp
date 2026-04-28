package com.testziliao.app.ui.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.testziliao.app.data.repository.ContentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class QuestionSetListViewModel(
    contentRepository: ContentRepository,
    private val categoryId: String,
    private val categoryName: String
) : ViewModel() {

    val uiState: StateFlow<QuestionSetListUiState> =
        contentRepository.observeQuestionSetsByCategory(categoryId)
            .map {
                QuestionSetListUiState(
                    questionSets = it,
                    categoryName = categoryName
                )
            }
            .catch {
                emit(
                    QuestionSetListUiState(
                        categoryName = categoryName,
                        message = it.message ?: "题库列表加载失败"
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = QuestionSetListUiState(categoryName = categoryName)
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
                    return QuestionSetListViewModel(
                        contentRepository = contentRepository,
                        categoryId = categoryId,
                        categoryName = categoryName
                    ) as T
                }
            }
        }
    }
}
