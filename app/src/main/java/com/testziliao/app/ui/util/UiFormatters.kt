package com.testziliao.app.ui.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

fun formatTimestamp(timestamp: Long): String {
    return dateTimeFormat.format(Date(timestamp))
}

fun parseTags(raw: String): List<String> {
    return raw.split("|")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
}
