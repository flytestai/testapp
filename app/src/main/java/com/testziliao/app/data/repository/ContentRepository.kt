package com.testziliao.app.data.repository

import com.testziliao.app.data.local.entity.ArticleContentEntity
import com.testziliao.app.data.local.entity.ArticleEntity
import com.testziliao.app.data.local.entity.CategoryEntity
import com.testziliao.app.data.local.entity.QuestionItemEntity
import com.testziliao.app.data.local.entity.QuestionSetEntity
import kotlinx.coroutines.flow.Flow

interface ContentRepository {
    fun observeCategories(): Flow<List<CategoryEntity>>
    fun observeArticles(): Flow<List<ArticleEntity>>
    fun observeFeaturedArticles(): Flow<List<ArticleEntity>>
    fun observeArticlesByCategory(categoryId: String): Flow<List<ArticleEntity>>
    fun observeQuestionSets(): Flow<List<QuestionSetEntity>>
    fun observeQuestionSetsByCategory(categoryId: String): Flow<List<QuestionSetEntity>>
    fun observeQuestionItems(questionSetId: String): Flow<List<QuestionItemEntity>>
    fun searchArticles(keyword: String): Flow<List<ArticleEntity>>
    fun searchQuestionSets(keyword: String): Flow<List<QuestionSetEntity>>
    fun searchQuestionItems(keyword: String): Flow<List<QuestionItemEntity>>
    suspend fun getArticle(articleId: String): ArticleEntity?
    suspend fun getArticleContent(articleId: String): ArticleContentEntity?
    suspend fun getQuestionSet(questionSetId: String): QuestionSetEntity?
    suspend fun refreshIndexContent()
    suspend fun refreshArticleContent(articleId: String)
    suspend fun refreshQuestionSetDetail(questionSetId: String)
}
