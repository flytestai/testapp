package com.testziliao.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.testziliao.app.data.repository.ContentRepository
import com.testziliao.app.data.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val contentRepository: ContentRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val keyword = MutableStateFlow("")

    val uiState: StateFlow<SearchUiState> = combine(
        keyword,
        searchRepository.observeRecentSearches(),
        keyword.flatMapLatest { query ->
            val normalized = query.trim()
            if (normalized.isEmpty()) {
                combine(
                    kotlinx.coroutines.flow.flowOf(emptyList()),
                    kotlinx.coroutines.flow.flowOf(emptyList()),
                    kotlinx.coroutines.flow.flowOf(emptyList())
                ) { articles, questionSets, questionItems ->
                    Triple(articles, questionSets, questionItems)
                }
            } else {
                combine(
                    contentRepository.searchArticles(normalized),
                    contentRepository.searchQuestionSets(normalized),
                    contentRepository.searchQuestionItems(normalized)
                ) { articles, questionSets, questionItems ->
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
