package com.testziliao.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_contents")
data class ArticleContentEntity(
    @PrimaryKey val articleId: String,
    val markdown: String,
    val updatedAt: String,
    val cachedAt: Long
)
