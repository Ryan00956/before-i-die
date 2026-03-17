package com.lastregrets.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastregrets.data.model.RegretCategory
import com.lastregrets.ui.components.formatResonateCount
import com.lastregrets.ui.components.getColor
import com.lastregrets.ui.components.getSourceLabel
import com.lastregrets.ui.theme.*
import com.lastregrets.ui.viewmodel.HomeUiState

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onRefresh: () -> Unit,
    onResonate: () -> Unit,
    onAddToTodo: () -> Unit,
    onNavigateToSquare: () -> Unit,
    onNavigateToPublish: () -> Unit,
    onDismissToast: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
                .padding(top = 48.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🕯️ 标题区域
            Text(
                text = "🕯️",
                fontSize = 48.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "临终清单",
                style = MaterialTheme.typography.displayLarge,
                color = WarmAmber,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "别等死了才后悔",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // 📊 数据概览
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
                    StatItem(count = "${uiState.totalRegrets}", label = "条遗憾")
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(SlateBlue)
                    )
                    StatItem(count = "匿名", label = "守护隐私")
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(SlateBlue)
                    )
                    StatItem(count = "真实", label = "直击内心")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🎴 今日遗憾卡片
            Text(
                text = "「 今日一条遗憾 」",
                style = MaterialTheme.typography.titleLarge,
                color = WarmAmber,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = WarmAmber,
                    modifier = Modifier.padding(32.dp)
                )
            } else {
                uiState.dailyRegret?.let { regret ->
                    val category = RegretCategory.fromName(regret.category)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkBlue),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            // 分类标签
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(category.getColor())
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${category.emoji} ${category.displayName}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = category.getColor()
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = getSourceLabel(regret.source),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextHint
                                )
                            }

                            // 遗憾内容
                            Text(
                                text = "「 ${regret.content} 」",
                                style = MaterialTheme.typography.bodyLarge,
                                color = CandleGlow,
                                lineHeight = 28.sp,
                                modifier = Modifier.padding(bottom = 20.dp)
                            )

                            // 操作按钮行
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 共鸣按钮
                                TextButton(
                                    onClick = onResonate,
                                    enabled = !uiState.hasResonated,
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = TextSecondary,
                                        disabledContentColor = ResonateColor
                                    )
                                ) {
                                    Icon(
                                        if (uiState.hasResonated) Icons.Default.Favorite
                                        else Icons.Default.FavoriteBorder,
                                        contentDescription = "共鸣",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (uiState.hasResonated) "已共鸣 ${formatResonateCount(regret.resonateCount)}"
                                               else "我也是 ${formatResonateCount(regret.resonateCount)}",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }

                                // 加入待办
                                Button(
                                    onClick = onAddToTodo,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = WarmAmber,
                                        contentColor = DeepNavy
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "加入待办",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "我现在能做",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 换一条
            TextButton(
                onClick = onRefresh,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "换一条",
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "换一条遗憾",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 快捷入口
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickEntryCard(
                    emoji = "📜",
                    title = "遗憾广场",
                    subtitle = "看看别人怎么说",
                    onClick = onNavigateToSquare,
                    modifier = Modifier.weight(1f)
                )
                QuickEntryCard(
                    emoji = "✏️",
                    title = "写下遗憾",
                    subtitle = "匿名分享",
                    onClick = onNavigateToPublish,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 底部寄语
            Text(
                text = "💡 你现在活着，你还有机会。",
                style = MaterialTheme.typography.bodyMedium,
                color = WarmAmber,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Text(
                text = "别让遗憾成为你的结局。",
                style = MaterialTheme.typography.bodySmall,
                color = TextHint,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Toast
        if (uiState.showAddedToast) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .padding(bottom = 80.dp),
                containerColor = SuccessGreen,
                contentColor = DeepNavy,
                action = {
                    TextButton(onClick = onDismissToast) {
                        Text("好的", color = DeepNavy, fontWeight = FontWeight.Bold)
                    }
                }
            ) {
                Text("已加入「别等死了才后悔」清单 ✓")
            }
        }
    }
}

@Composable
private fun StatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleLarge,
            color = WarmAmber,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextHint
        )
    }
}

@Composable
private fun QuickEntryCard(
    emoji: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightBlue)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextHint
            )
        }
    }
}
