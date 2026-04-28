package com.testziliao.app.data.mapper

import com.testziliao.app.data.local.entity.ArticleEntity
import com.testziliao.app.data.local.entity.CategoryEntity
import com.testziliao.app.data.local.entity.QuestionItemEntity
import com.testziliao.app.data.local.entity.QuestionSetEntity
import com.testziliao.app.data.model.remote.ArticleDto
import com.testziliao.app.data.model.remote.CategoryDto
import com.testziliao.app.data.model.remote.QuestionItemDto
import com.testziliao.app.data.model.remote.QuestionSetDetailDto
import com.testziliao.app.data.model.remote.QuestionSetDto

private const val TAG_SEPARATOR = "|"

fun CategoryDto.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        type = type
    )
}

fun ArticleDto.toEntity(cachedAt: Long): ArticleEntity {
    return ArticleEntity(
        id = id,
        title = title,
        summary = summary,
        categoryId = categoryId,
        tags = tags.joinToString(TAG_SEPARATOR),
        cover = cover,
        author = author,
        publishedAt = publishedAt,
        updatedAt = updatedAt,
        markdownUrl = markdownUrl,
        featured = featured,
        cachedAt = cachedAt
    )
}

fun QuestionSetDto.toEntity(cachedAt: Long): QuestionSetEntity {
    return QuestionSetEntity(
        id = id,
        title = title,
        categoryId = categoryId,
        questionCount = questionCount,
        updatedAt = updatedAt,
        fileUrl = fileUrl,
        cachedAt = cachedAt
    )
}

fun QuestionSetDetailDto.toEntities(cachedAt: Long): List<QuestionItemEntity> {
    return questions.mapIndexed { index, item ->
        item.toEntity(
            questionSetId = id,
            sortOrder = index,
            cachedAt = cachedAt
        )
    }
}

fun QuestionItemDto.toEntity(
    questionSetId: String,
    sortOrder: Int,
    cachedAt: Long
): QuestionItemEntity {
    return QuestionItemEntity(
        id = id,
        questionSetId = questionSetId,
        question = question,
        answer = answer,
        tags = tags.joinToString(TAG_SEPARATOR),
        difficulty = difficulty,
        sortOrder = sortOrder,
        cachedAt = cachedAt
    )
}
