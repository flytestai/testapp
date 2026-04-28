package com.testziliao.app.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionItemDto(
    @SerialName("id")
    val id: String,
    @SerialName("question")
    val question: String,
    @SerialName("answer")
    val answer: String,
    @SerialName("tags")
    val tags: List<String> = emptyList(),
    @SerialName("difficulty")
    val difficulty: String = "easy"
)
