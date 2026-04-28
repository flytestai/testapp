package com.testziliao.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_articles")
data class FavoriteArticleEntity(
    @PrimaryKey val articleId: String,
    val createdAt: Long
)
