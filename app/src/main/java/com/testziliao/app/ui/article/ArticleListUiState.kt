package com.testziliao.app.ui.article

import com.testziliao.app.data.local.entity.ArticleEntity

data class ArticleListUiState(
    val articles: List<ArticleEntity> = emptyList(),
    val categoryName: String = "",
    val message: String? = null
)
