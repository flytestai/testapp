package com.testziliao.app.data.model.local

import androidx.room.Embedded
import androidx.room.Relation
import com.testziliao.app.data.local.entity.ArticleEntity
import com.testziliao.app.data.local.entity.CategoryEntity

data class ArticleWithCategory(
    @Embedded val article: ArticleEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity?
)
