package com.lastregrets.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastregrets.data.model.Regret
import com.lastregrets.data.model.RegretCategory
import com.lastregrets.ui.components.formatResonateCount
import com.lastregrets.ui.components.getColor
import com.lastregrets.ui.components.getSourceLabel
import com.lastregrets.ui.theme.*
import com.lastregrets.ui.viewmodel.RegretSquareUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegretSquareScreen(
    uiState: RegretSquareUiState,
    onSelectCategory: (RegretCategory?) -> Unit,
    onResonate: (Regret) -> Unit,
    onAddToTodo: (Regret) -> Unit,
    onDismissToast: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 顶部标题
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 48.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "📜 遗憾广场",
                    style = MaterialTheme.typography.headlineLarge,
                    color = WarmAmber,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "那些来不及说出口的话，来不及做完的事",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextHint,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // 分类筛选
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // "全部"标签
                FilterChip(
                    selected = uiState.selectedCategory == null,
                    onClick = { onSelectCategory(null) },
                    label = { Text("全部") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = WarmAmber,
                        selectedLabelColor = DeepNavy,
                        containerColor = MidnightBlue,
                        labelColor = TextSecondary
                    )
                )

                RegretCategory.entries.forEach { category ->
                    FilterChip(
                        selected = uiState.selectedCategory == category,
                        onClick = { onSelectCategory(category) },
                        label = { Text("${category.emoji} ${category.displayName}") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = category.getColor(),
                            selectedLabelColor = DeepNavy,
                            containerColor = MidnightBlue,
                            labelColor = TextSecondary
                        )
                    )
                }
            }

            // 遗憾列表
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = WarmAmber)
                }
            } else if (uiState.regrets.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "📭", fontSize = 48.sp)
                        Text(
                            text = "这个分类暂时没有遗憾",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextHint,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 80.dp)
                ) {
                    items(
                        uiState.regrets,
                        key = { it.firestoreId ?: it.id.toString() }
                    ) { regret ->
                        RegretCard(
                            regret = regret,
                            onResonate = { onResonate(regret) },
                            onAddToTodo = { onAddToTodo(regret) }
                        )
                    }
                }
            }
        }

        // Toast
        uiState.toastMessage?.let { message ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .padding(bottom = 80.dp),
                containerColor = if (message.contains("✓")) SuccessGreen else WarmAmber,
                contentColor = DeepNavy,
                action = {
                    TextButton(onClick = onDismissToast) {
                        Text("知道了", color = DeepNavy, fontWeight = FontWeight.Bold)
                    }
                }
            ) {
                Text(message)
            }
        }
    }
}

@Composable
private fun RegretCard(
    regret: Regret,
    onResonate: () -> Unit,
    onAddToTodo: () -> Unit
) {
    val category = RegretCategory.fromName(regret.category)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkBlue)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 顶部：分类 + 来源
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(category.getColor())
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${category.emoji} ${category.displayName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = category.getColor()
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = getSourceLabel(regret.source),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextHint
                )
            }

            // 内容
            Text(
                text = regret.content,
                style = MaterialTheme.typography.bodyLarge,
                color = CandleGlow,
                lineHeight = 26.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // 底部操作
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onResonate,
                    colors = ButtonDefaults.textButtonColors(contentColor = ResonateColor),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "我也是 ${formatResonateCount(regret.resonateCount)}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                TextButton(
                    onClick = onAddToTodo,
                    colors = ButtonDefaults.textButtonColors(contentColor = WarmAmber),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(
                        Icons.Default.AddTask,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "我现在能做",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
