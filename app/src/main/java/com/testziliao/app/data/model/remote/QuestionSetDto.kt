package com.testziliao.app.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionSetDto(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("category_id")
    val categoryId: String,
    @SerialName("question_count")
    val questionCount: Int,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("file_url")
    val fileUrl: String
)
