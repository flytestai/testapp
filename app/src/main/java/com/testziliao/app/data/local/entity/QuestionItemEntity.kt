package com.testziliao.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "question_items",
    indices = [Index("questionSetId")]
)
data class QuestionItemEntity(
    @PrimaryKey val id: String,
    val questionSetId: String,
    val question: String,
    val answer: String,
    val tags: String,
    val difficulty: String,
    val sortOrder: Int,
    val cachedAt: Long
)
