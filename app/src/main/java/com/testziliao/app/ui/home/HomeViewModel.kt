package com.testziliao.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.testziliao.app.data.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val contentRepository: ContentRepository
) : ViewModel() {

    private val refreshState = MutableStateFlow(HomeUiState(isRefreshing = true))

    val uiState: StateFlow<HomeUiState> = combine(
        contentRepository.observeFeaturedArticles(),
        contentRepository.observeArticles(),
        contentRepository.observeQuestionSets(),
        refreshState
    ) { featured, articles, questionSets, refresh ->
        HomeUiState(
            featuredArticles = featured.take(5),
            latestArticles = articles.take(10),
            latestQuestionSets = questionSets.take(6),
            isRefreshing = refresh.isRefreshing,
            message = refresh.message,
            lastRefreshAt = refresh.lastRefreshAt
        )
    }.catch {
        emit(HomeUiState(message = it.message ?: "内容加载失败"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(isRefreshing = true)
    )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            refreshState.update { it.copy(isRefreshing = true, message = null) }
            runCatching {
                contentRepository.refreshIndexContent()
            }.onSuccess {
                refreshState.update {
                    it.copy(
                        isRefreshing = false,
                        lastRefreshAt = System.currentTimeMillis(),
                        message = "内容已更�?
                    )
                }
            }.onFailure {
                refreshState.update { state ->
                    state.copy(
                        isRefreshing = false,
                        message = it.message ?: "刷新失败"
                    )
                }
            }
        }
    }

    companion object {
        fun factory(contentRepository: ContentRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(contentRepository) as T
                }
            }
        }
    }
}
