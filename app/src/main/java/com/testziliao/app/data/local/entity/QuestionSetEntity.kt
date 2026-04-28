package com.testziliao.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_sets")
data class QuestionSetEntity(
    @PrimaryKey val id: String,
    val title: String,
    val categoryId: String,
    val questionCount: Int,
    val updatedAt: String,
    val fileUrl: String,
    val cachedAt: Long
)
