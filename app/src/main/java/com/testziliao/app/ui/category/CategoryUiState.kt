package com.testziliao.app.ui.category

import com.testziliao.app.data.local.entity.CategoryEntity

data class CategoryUiState(
    val categories: List<CategoryEntity> = emptyList(),
    val message: String? = null
)
