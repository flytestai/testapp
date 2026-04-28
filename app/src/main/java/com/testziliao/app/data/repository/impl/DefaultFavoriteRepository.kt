package com.testziliao.app.data.repository.impl

import com.testziliao.app.data.local.dao.FavoriteArticleDao
import com.testziliao.app.data.local.dao.FavoriteQuestionSetDao
import com.testziliao.app.data.local.entity.FavoriteArticleEntity
import com.testziliao.app.data.local.entity.FavoriteQuestionSetEntity
import com.testziliao.app.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class DefaultFavoriteRepository(
    private val favoriteArticleDao: FavoriteArticleDao,
    private val favoriteQuestionSetDao: FavoriteQuestionSetDao
) : FavoriteRepository {

    override fun observeFavoriteArticles(): Flow<List<FavoriteArticleEntity>> {
        return favoriteArticleDao.observeAll()
    }

    override fun observeFavoriteQuestionSets(): Flow<List<FavoriteQuestionSetEntity>> {
        return favoriteQuestionSetDao.observeAll()
    }

    override fun observeIsArticleFavorite(articleId: String): Flow<Boolean> {
        return favoriteArticleDao.observeIsFavorite(articleId)
    }

    override fun observeIsQuestionSetFavorite(questionSetId: String): Flow<Boolean> {
        return favoriteQuestionSetDao.observeIsFavorite(questionSetId)
    }

    override suspend fun toggleArticleFavorite(articleId: String) {
        if (favoriteArticleDao.isFavorite(articleId)) {
            favoriteArticleDao.deleteByArticleId(articleId)
        } else {
            favoriteArticleDao.insert(
                FavoriteArticleEntity(
                    articleId = articleId,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun toggleQuestionSetFavorite(questionSetId: String) {
        if (favoriteQuestionSetDao.isFavorite(questionSetId)) {
            favoriteQuestionSetDao.deleteByQuestionSetId(questionSetId)
        } else {
            favoriteQuestionSetDao.insert(
                FavoriteQuestionSetEntity(
                    questionSetId = questionSetId,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }
}
