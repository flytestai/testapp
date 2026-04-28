package com.testziliao.app.ui.search

import com.testziliao.app.data.local.entity.ArticleEntity
import com.testziliao.app.data.local.entity.QuestionItemEntity
import com.testziliao.app.data.local.entity.QuestionSetEntity
import com.testziliao.app.data.local.entity.SearchHistoryEntity

data class SearchUiState(
    val keyword: String = "",
    val articleResults: List<ArticleEntity> = emptyList(),
    val questionSetResults: List<QuestionSetEntity> = emptyList(),
    val questionItemResults: List<QuestionItemEntity> = emptyList(),
    val recentSearches: List<SearchHistoryEntity> = emptyList()
)
