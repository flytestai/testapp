package com.testziliao.app.data.model.local

import androidx.room.Embedded
import androidx.room.Relation
import com.testziliao.app.data.local.entity.QuestionItemEntity
import com.testziliao.app.data.local.entity.QuestionSetEntity

data class QuestionSetWithItems(
    @Embedded val questionSet: QuestionSetEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "questionSetId"
    )
    val items: List<QuestionItemEntity>
)
