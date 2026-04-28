package com.testziliao.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.testziliao.app.data.local.entity.QuestionItemEntity
import com.testziliao.app.ui.util.parseTags

@Composable
fun QuestionItemCard(item: QuestionItemEntity, modifier: Modifier = Modifier) {
    val expanded = remember(item.id) { mutableStateOf(false) }
    val tags = parseTags(item.tags)

    Card(
        modifier = modifier.fillMaxWidth().clickable { expanded.value = !expanded.value },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = { expanded.value = !expanded.value },
                    label = {
                        Text(
                            when (item.difficulty.lowercase()) {
                                "easy" -> "基础"
                                "medium" -> "进阶"
                                "hard" -> "高阶"
                                else -> item.difficulty
                            }
                        )
                    }
                )
                if (tags.isNotEmpty()) {
                    AssistChip(onClick = { expanded.value = !expanded.value }, label = { Text(tags.first()) })
                }
            }
            Text(text = item.question, style = MaterialTheme.typography.titleMedium)
            if (tags.size > 1) {
                Text(text = tags.drop(1).joinToString("  "), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
            if (expanded.value) {
                Text(text = item.answer, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            } else {
                Text(text = "点击展开答案", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
