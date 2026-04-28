package com.testziliao.app.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.testziliao.app.data.local.entity.HistoryRecordEntity
import com.testziliao.app.data.model.AppConstants
import com.testziliao.app.data.repository.ContentRepository
import com.testziliao.app.data.repository.FavoriteRepository
import com.testziliao.app.data.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ArticleDetailViewModel(
    private val articleId: String,
    private val contentRepository: ContentRepository,
    private val favoriteRepository: FavoriteRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArticleDetailUiState())
    val uiState: StateFlow<ArticleDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            val article = contentRepository.getArticle(articleId)
            if (article == null) {
                _uiState.value = ArticleDetailUiState(
                    isLoading = false,
                    message = "文章不存�?
                )
                return@launch
            }

            val favorite = favoriteRepository.observeIsArticleFavorite(articleId).first()
            _uiState.value = _uiState.value.copy(
                article = article,
                isFavorite = favorite,
                isLoading = true
            )

            recordHistory(article)

            val cachedContent = contentRepository.getArticleContent(articleId)
            if (cachedContent != null) {
                _uiState.value = _uiState.value.copy(
                    markdown = cachedContent.markdown,
                    isLoading = false
                )
            }

            runCatching {
                contentRepository.refreshArticleContent(articleId)
                contentRepository.getArticleContent(articleId)
            }.onSuccess { latest ->
                if (latest != null) {
                    _uiState.value = _uiState.value.copy(
                        markdown = latest.markdown,
                        isLoading = false,
                        message = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = it.message ?: "正文加载失败"
                )
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            favoriteRepository.toggleArticleFavorite(articleId)
            val favorite = favoriteRepository.observeIsArticleFavorite(articleId).first()
            _uiState.value = _uiState.value.copy(isFavorite = favorite)
        }
    }

    private suspend fun recordHistory(article: com.testziliao.app.data.local.entity.ArticleEntity) {
        historyRepository.addHistory(
            HistoryRecordEntity(
                targetId = article.id,
                targetType = AppConstants.TARGET_TYPE_ARTICLE,
                title = article.title,
                subtitle = article.summary,
                cover = article.cover,
                viewedAt = System.currentTimeMillis()
            )
        )
    }

    companion object {
        fun factory(
            articleId: String,
            contentRepository: ContentRepository,
            favoriteRepository: FavoriteRepository,
            historyRepository: HistoryRepository
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ArticleDetailViewModel(
                        articleId = articleId,
                        contentRepository = contentRepository,
                        favoriteRepository = favoriteRepository,
                        historyRepository = historyRepository
                    ) as T
                }
            }
        }
    }
}
