package com.testziliao.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val categoryId: String,
    val tags: String,
    val cover: String?,
    val author: String,
    val publishedAt: String,
    val updatedAt: String,
    val markdownUrl: String,
    val featured: Boolean,
    val cachedAt: Long
)
