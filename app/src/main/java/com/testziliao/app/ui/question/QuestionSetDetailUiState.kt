package com.testziliao.app.ui.question

import com.testziliao.app.data.local.entity.QuestionItemEntity
import com.testziliao.app.data.local.entity.QuestionSetEntity

data class QuestionSetDetailUiState(
    val questionSet: QuestionSetEntity? = null,
    val allItems: List<QuestionItemEntity> = emptyList(),
    val items: List<QuestionItemEntity> = emptyList(),
    val selectedTag: String = "全部",
    val availableTags: List<String> = listOf("全部"),
    val isFavorite: Boolean = false,
    val isLoading: Boolean = true,
    val message: String? = null
)
