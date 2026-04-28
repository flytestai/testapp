package com.testziliao.app.data.repository

import com.testziliao.app.data.local.entity.FavoriteArticleEntity
import com.testziliao.app.data.local.entity.FavoriteQuestionSetEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun observeFavoriteArticles(): Flow<List<FavoriteArticleEntity>>
    fun observeFavoriteQuestionSets(): Flow<List<FavoriteQuestionSetEntity>>
    fun observeIsArticleFavorite(articleId: String): Flow<Boolean>
    fun observeIsQuestionSetFavorite(questionSetId: String): Flow<Boolean>
    suspend fun toggleArticleFavorite(articleId: String)
    suspend fun toggleQuestionSetFavorite(questionSetId: String)
}
