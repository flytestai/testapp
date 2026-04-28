package com.testziliao.app.ui.question

import com.testziliao.app.data.local.entity.QuestionItemEntity
import com.testziliao.app.data.local.entity.QuestionSetEntity

data class QuestionSetDetailUiState(
    val questionSet: QuestionSetEntity? = null,
    val allItems: List<QuestionItemEntity> = emptyList(),
    val items: List<QuestionItemEntity> = emptyList(),
    val selectedTag: String = "全部",
    val availableTags: List<String> = listOf("全部"),
    val currentIndex: Int = 0,
    val currentItem: QuestionItemEntity? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = true,
    val message: String? = null
) {
    val canGoPrevious: Boolean
        get() = currentIndex > 0

    val canGoNext: Boolean
        get() = currentIndex < items.lastIndex

    val progressLabel: String
        get() = if (items.isEmpty()) "0 / 0" else "${currentIndex + 1} / ${items.size}"
}
