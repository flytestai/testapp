package com.testziliao.app.ui.favorites

import com.testziliao.app.data.local.entity.ArticleEntity
import com.testziliao.app.data.local.entity.FavoriteArticleEntity
import com.testziliao.app.data.local.entity.FavoriteQuestionSetEntity
import com.testziliao.app.data.local.entity.QuestionSetEntity

data class FavoritesUiState(
    val articleFavorites: List<FavoriteArticleEntity> = emptyList(),
    val questionSetFavorites: List<FavoriteQuestionSetEntity> = emptyList(),
    val favoriteArticles: List<ArticleEntity> = emptyList(),
    val favoriteQuestionSets: List<QuestionSetEntity> = emptyList()
)
