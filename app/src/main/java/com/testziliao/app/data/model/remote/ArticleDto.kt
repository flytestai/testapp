package com.testziliao.app.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("summary")
    val summary: String,
    @SerialName("category_id")
    val categoryId: String,
    @SerialName("tags")
    val tags: List<String> = emptyList(),
    @SerialName("cover")
    val cover: String? = null,
    @SerialName("author")
    val author: String,
    @SerialName("published_at")
    val publishedAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("markdown_url")
    val markdownUrl: String,
    @SerialName("featured")
    val featured: Boolean = false
)
