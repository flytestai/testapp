package com.testziliao.app.ui.question

data class QuestionSetTagFilter(
    val selectedTag: String = "全部",
    val availableTags: List<String> = listOf("全部")
)
