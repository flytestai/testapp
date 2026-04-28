package com.testziliao.app.ui.home

import com.testziliao.app.data.local.entity.ArticleEntity
import com.testziliao.app.data.local.entity.QuestionSetEntity

data class HomeUiState(
    val featuredArticles: List<ArticleEntity> = emptyList(),
    val latestArticles: List<ArticleEntity> = emptyList(),
    val latestQuestionSets: List<QuestionSetEntity> = emptyList(),
    val isRefreshing: Boolean = false,
    val message: String? = null,
    val lastRefreshAt: Long? = null
)
