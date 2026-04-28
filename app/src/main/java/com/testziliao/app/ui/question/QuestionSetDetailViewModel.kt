package com.testziliao.app.ui.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.testziliao.app.data.local.entity.HistoryRecordEntity
import com.testziliao.app.data.model.AppConstants
import com.testziliao.app.data.repository.ContentRepository
import com.testziliao.app.data.repository.FavoriteRepository
import com.testziliao.app.data.repository.HistoryRepository
import com.testziliao.app.ui.util.parseTags
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class QuestionSetDetailViewModel(
    private val questionSetId: String,
    private val contentRepository: ContentRepository,
    private val favoriteRepository: FavoriteRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestionSetDetailUiState())
    val uiState: StateFlow<QuestionSetDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            val questionSet = contentRepository.getQuestionSet(questionSetId)
            if (questionSet == null) {
                _uiState.value = QuestionSetDetailUiState(
                    isLoading = false,
                    message = "题库不存�?
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                questionSet = questionSet,
                isFavorite = favoriteRepository.observeIsQuestionSetFavorite(questionSetId).first(),
                isLoading = true
            )

            historyRepository.addHistory(
                HistoryRecordEntity(
                    targetId = questionSet.id,
                    targetType = AppConstants.TARGET_TYPE_QUESTION_SET,
                    title = questionSet.title,
                    subtitle = "�?${questionSet.questionCount} �?,
                    cover = null,
                    viewedAt = System.currentTimeMillis()
                )
            )

            val cachedItems = contentRepository.observeQuestionItems(questionSetId).first()
            if (cachedItems.isNotEmpty()) {
                updateQuestionItems(cachedItems, selectedTag = _uiState.value.selectedTag, loading = false)
            }

            runCatching {
                contentRepository.refreshQuestionSetDetail(questionSetId)
                contentRepository.observeQuestionItems(questionSetId).first()
            }.onSuccess { latest ->
                updateQuestionItems(latest, selectedTag = _uiState.value.selectedTag, loading = false, message = null)
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = it.message ?: "题目加载失败"
                )
            }
        }
    }

    fun selectTag(tag: String) {
        updateQuestionItems(
            source = _uiState.value.allItems,
            selectedTag = tag,
            loading = false,
            message = _uiState.value.message
        )
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            favoriteRepository.toggleQuestionSetFavorite(questionSetId)
            _uiState.value = _uiState.value.copy(
                isFavorite = favoriteRepository.observeIsQuestionSetFavorite(questionSetId).first()
            )
        }
    }

    private fun updateQuestionItems(
        source: List<QuestionItemEntity>,
        selectedTag: String,
        loading: Boolean,
        message: String? = _uiState.value.message
    ) {
        val tags = buildList {
            add("全部")
            source.flatMapTo(this) { parseTags(it.tags) }
        }.distinct()

        val filteredItems = if (selectedTag == "全部") {
            source
        } else {
            source.filter { parseTags(it.tags).contains(selectedTag) }
        }

        _uiState.value = _uiState.value.copy(
            allItems = source,
            items = filteredItems,
            selectedTag = selectedTag,
            availableTags = tags,
            isLoading = loading,
            message = message
        )
    }

    companion object {
        fun factory(
            questionSetId: String,
            contentRepository: ContentRepository,
            favoriteRepository: FavoriteRepository,
            historyRepository: HistoryRepository
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return QuestionSetDetailViewModel(
                        questionSetId = questionSetId,
                        contentRepository = contentRepository,
                        favoriteRepository = favoriteRepository,
                        historyRepository = historyRepository
                    ) as T
                }
            }
        }
    }
}
