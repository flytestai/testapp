package com.testziliao.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_question_sets")
data class FavoriteQuestionSetEntity(
    @PrimaryKey val questionSetId: String,
    val createdAt: Long
)
