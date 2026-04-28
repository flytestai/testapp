package com.testziliao.app.data.repository.impl

import com.testziliao.app.data.local.dao.ArticleContentDao
import com.testziliao.app.data.local.dao.ArticleDao
import com.testziliao.app.data.local.dao.CategoryDao
import com.testziliao.app.data.local.dao.QuestionItemDao
import com.testziliao.app.data.local.dao.QuestionSetDao
import com.testziliao.app.data.local.entity.ArticleContentEntity
import com.testziliao.app.data.local.entity.ArticleEntity
import com.testziliao.app.data.local.entity.CategoryEntity
import com.testziliao.app.data.local.entity.QuestionItemEntity
import com.testziliao.app.data.local.entity.QuestionSetEntity
import com.testziliao.app.data.mapper.toEntities
import com.testziliao.app.data.mapper.toEntity
import com.testziliao.app.data.remote.ContentApiService
import com.testziliao.app.data.remote.ContentUrlResolver
import com.testziliao.app.data.repository.ContentRepository
import kotlinx.coroutines.flow.Flow

class DefaultContentRepository(
    private val apiService: ContentApiService,
    private val categoryDao: CategoryDao,
    private val articleDao: ArticleDao,
    private val articleContentDao: ArticleContentDao,
    private val questionSetDao: QuestionSetDao,
    private val questionItemDao: QuestionItemDao
) : ContentRepository {

    override fun observeCategories(): Flow<List<CategoryEntity>> = categoryDao.observeAll()

    override fun observeArticles(): Flow<List<ArticleEntity>> = articleDao.observeAll()

    override fun observeFeaturedArticles(): Flow<List<ArticleEntity>> = articleDao.observeFeatured()

    override fun observeArticlesByCategory(categoryId: String): Flow<List<ArticleEntity>> {
        return articleDao.observeByCategory(categoryId)
    }

    override fun observeQuestionSets(): Flow<List<QuestionSetEntity>> = questionSetDao.observeAll()

    override fun observeQuestionSetsByCategory(categoryId: String): Flow<List<QuestionSetEntity>> {
        return questionSetDao.observeByCategory(categoryId)
    }

    override fun observeQuestionItems(questionSetId: String): Flow<List<QuestionItemEntity>> {
        return questionItemDao.observeByQuestionSetId(questionSetId)
    }

    override fun searchArticles(keyword: String): Flow<List<ArticleEntity>> = articleDao.search(keyword)

    override fun searchQuestionSets(keyword: String): Flow<List<QuestionSetEntity>> {
        return questionSetDao.search(keyword)
    }

    override fun searchQuestionItems(keyword: String): Flow<List<QuestionItemEntity>> {
        return questionItemDao.search(keyword)
    }

    override suspend fun getArticle(articleId: String): ArticleEntity? = articleDao.getById(articleId)

    override suspend fun getArticleContent(articleId: String): ArticleContentEntity? {
        return articleContentDao.getByArticleId(articleId)
    }

    override suspend fun getQuestionSet(questionSetId: String): QuestionSetEntity? {
        return questionSetDao.getById(questionSetId)
    }

    override suspend fun refreshIndexContent() {
        val cachedAt = System.currentTimeMillis()
        val categories = apiService.getCategories().map { it.toEntity() }
        val articles = apiService.getArticles().map { it.toEntity(cachedAt) }
        val questionSets = apiService.getQuestionSets().map { it.toEntity(cachedAt) }

        categoryDao.clearAll()
        categoryDao.insertAll(categories)

        articleDao.clearAll()
        articleDao.insertAll(articles)

        questionSetDao.clearAll()
        questionSetDao.insertAll(questionSets)
    }

    override suspend fun refreshArticleContent(articleId: String) {
        val article = articleDao.getById(articleId) ?: return
        val markdown = apiService.getArticleMarkdown(
            ContentUrlResolver.resolve(article.markdownUrl)
        )
        articleContentDao.insert(
            ArticleContentEntity(
                articleId = articleId,
                markdown = markdown,
                updatedAt = article.updatedAt,
                cachedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun refreshQuestionSetDetail(questionSetId: String) {
        val questionSet = questionSetDao.getById(questionSetId) ?: return
        val detail = apiService.getQuestionSetDetail(
            ContentUrlResolver.resolve(questionSet.fileUrl)
        )
        val cachedAt = System.currentTimeMillis()

        questionItemDao.deleteByQuestionSetId(questionSetId)
        questionItemDao.insertAll(detail.toEntities(cachedAt))
    }
}
