package com.testziliao.app.data.remote

import com.testziliao.app.data.model.remote.ArticleDto
import com.testziliao.app.data.model.remote.CategoryDto
import com.testziliao.app.data.model.remote.QuestionSetDetailDto
import com.testziliao.app.data.model.remote.QuestionSetDto
import retrofit2.http.GET
import retrofit2.http.Url

interface ContentApiService {
    @GET("content/categories/index.json")
    suspend fun getCategories(): List<CategoryDto>

    @GET("content/articles/index.json")
    suspend fun getArticles(): List<ArticleDto>

    @GET("content/questions/index.json")
    suspend fun getQuestionSets(): List<QuestionSetDto>

    @GET
    suspend fun getArticleMarkdown(@Url url: String): String

    @GET
    suspend fun getQuestionSetDetail(@Url url: String): QuestionSetDetailDto
}
