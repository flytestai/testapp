package com.testziliao.app.ui.question

import com.testziliao.app.data.local.entity.QuestionSetEntity

data class QuestionSetListUiState(
    val questionSets: List<QuestionSetEntity> = emptyList(),
    val categoryName: String = "",
    val message: String? = null
)
