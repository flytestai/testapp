package com.testziliao.app.ui.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatTimestamp(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault())
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

fun parseTags(raw: String): List<String> {
    return raw.split("|")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
}

fun formatCategoryLabel(categoryId: String): String {
    return when (categoryId) {
        "theory" -> "测试理论"
        "api" -> "接口测试"
        "automation" -> "自动化测试"
        "performance" -> "性能测试"
        "interview" -> "面试题库"
        else -> categoryId.ifBlank { "内容" }
    }
}
