package com.testziliao.app.data.model.local

data class ContentListItem(
    val id: String,
    val title: String,
    val summary: String?,
    val cover: String?,
    val categoryId: String,
    val updatedAt: String,
    val targetType: String
)
