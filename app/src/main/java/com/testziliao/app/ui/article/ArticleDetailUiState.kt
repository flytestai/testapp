package com.testziliao.app.ui.article

import com.testziliao.app.data.local.entity.ArticleEntity

data class ArticleDetailUiState(
    val article: ArticleEntity? = null,
    val markdown: String = "",
    val isFavorite: Boolean = false,
    val isLoading: Boolean = true,
    val message: String? = null
)
