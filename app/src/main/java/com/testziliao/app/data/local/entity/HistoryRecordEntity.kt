package com.testziliao.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "history_records",
    indices = [Index("targetId"), Index("viewedAt")]
)
data class HistoryRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val targetId: String,
    val targetType: String,
    val title: String,
    val subtitle: String?,
    val cover: String?,
    val viewedAt: Long
)
