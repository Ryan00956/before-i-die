package com.lastregrets.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastregrets.data.model.TodoItem
import com.lastregrets.ui.theme.*
import com.lastregrets.ui.viewmodel.TodoUiState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TodoScreen(
    uiState: TodoUiState,
    onToggleComplete: (TodoItem) -> Unit,
    onDelete: (Long) -> Unit,
    onToggleShowCompleted: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 48.dp)
    ) {
        // 标题
        Text(
            text = "✅ 别等死了才后悔",
            style = MaterialTheme.typography.headlineLarge,
            color = WarmAmber,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "你的行动清单 — 现在就去做",
            style = MaterialTheme.typography.bodySmall,
            color = TextHint,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        // 统计卡片
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkBlue)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${uiState.totalCount}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = WarmAmber,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "总计划",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextHint
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${uiState.completedCount}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "已挽救",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextHint
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${uiState.activeTodos.size}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = RoseGold,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "进行中",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextHint
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.activeTodos.isEmpty() && uiState.completedTodos.isEmpty()) {
            // 空状态
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "📋", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "还没有待办事项",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "去遗憾广场看看\n找到你现在能做的事",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextHint,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                // 进行中
                if (uiState.activeTodos.isNotEmpty()) {
                    item {
                        Text(
                            text = "🔥 进行中 (${uiState.activeTodos.size})",
                            style = MaterialTheme.typography.titleMedium,
                            color = RoseGold,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(uiState.activeTodos, key = { it.id }) { todo ->
                        TodoItemCard(
                            todo = todo,
                            onToggle = { onToggleComplete(todo) },
                            onDelete = { onDelete(todo.id) }
                        )
                    }
                }

                // 已完成
                if (uiState.completedTodos.isNotEmpty()) {
                    item {
                        TextButton(
                            onClick = onToggleShowCompleted,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Icon(
                                if (uiState.showCompleted) Icons.Default.ExpandLess
                                else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "✓ 已挽救的遗憾 (${uiState.completedTodos.size})",
                                style = MaterialTheme.typography.titleMedium,
                                color = SuccessGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    if (uiState.showCompleted) {
                        items(uiState.completedTodos, key = { it.id }) { todo ->
                            TodoItemCard(
                                todo = todo,
                                onToggle = { onToggleComplete(todo) },
                                onDelete = { onDelete(todo.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TodoItemCard(
    todo: TodoItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (todo.isCompleted) MidnightBlue else DarkBlue,
        label = "bg"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 复选框
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = SuccessGreen,
                    uncheckedColor = SlateBlue,
                    checkmarkColor = DeepNavy
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 内容
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = todo.regretContent,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (todo.isCompleted) TextHint else CandleGlow,
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null,
                    lineHeight = 22.sp
                )

                if (todo.isCompleted && todo.completedAt != null) {
                    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                    Text(
                        text = "✓ 完成于 ${dateFormat.format(Date(todo.completedAt))}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SuccessGreen,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // 删除按钮
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "删除",
                    tint = TextHint,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
