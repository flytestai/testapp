package com.testziliao.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.testziliao.app.data.local.entity.ArticleEntity
import com.testziliao.app.data.local.entity.QuestionItemEntity
import com.testziliao.app.data.local.entity.QuestionSetEntity
import com.testziliao.app.data.repository.ContentRepository
import com.testziliao.app.data.repository.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val contentRepository: ContentRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val keyword = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<SearchUiState> = combine(
        keyword,
        searchRepository.observeRecentSearches(),
        keyword.flatMapLatest { query ->
            val normalized = query.trim()
            if (normalized.isEmpty()) {
                combine(
                    flowOf(emptyList<ArticleEntity>()),
                    flowOf(emptyList<QuestionSetEntity>()),
                    flowOf(emptyList<QuestionItemEntity>())
                ) { articles: List<ArticleEntity>, questionSets: List<QuestionSetEntity>, questionItems: List<QuestionItemEntity> ->
                    Triple(articles, questionSets, questionItems)
                }
            } else {
                combine(
                    contentRepository.searchArticles(normalized),
                    contentRepository.searchQuestionSets(normalized),
                    contentRepository.searchQuestionItems(normalized)
                ) { articles: List<ArticleEntity>, questionSets: List<QuestionSetEntity>, questionItems: List<QuestionItemEntity> ->
                    Triple(articles, questionSets, questionItems)
                }
            }
        }
    ) { query, recent, results ->
        SearchUiState(
            keyword = query,
            recentSearches = recent,
            articleResults = results.first,
            questionSetResults = results.second,
            questionItemResults = results.third
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SearchUiState()
    )

    fun onKeywordChange(value: String) {
        keyword.update { value }
    }

    fun submitKeyword() {
        val value = keyword.value.trim()
        if (value.isBlank()) return
        viewModelScope.launch {
            searchRepository.saveKeyword(value)
        }
    }

    fun useKeyword(value: String) {
        keyword.value = value
    }

    fun clearRecentSearches() {
        viewModelScope.launch {
            searchRepository.clearSearchHistory()
        }
    }

    companion object {
        fun factory(
            contentRepository: ContentRepository,
            searchRepository: SearchRepository
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(contentRepository, searchRepository) as T
                }
            }
        }
    }
}
