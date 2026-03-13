package com.lastregrets.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastregrets.data.model.RegretCategory
import com.lastregrets.ui.components.getColor
import com.lastregrets.ui.theme.*
import com.lastregrets.ui.viewmodel.InsightsUiState

@Composable
fun InsightsScreen(uiState: InsightsUiState) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .padding(top = 48.dp, bottom = 100.dp)
    ) {
        // 标题
        Text(
            text = "📊 数据洞察",
            style = MaterialTheme.typography.headlineLarge,
            color = WarmAmber,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "那些被大多数人后悔的事",
            style = MaterialTheme.typography.bodySmall,
            color = TextHint,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        // 概览统计
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                emoji = "📝",
                value = "${uiState.totalRegrets}",
                label = "总遗憾数",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                emoji = "✅",
                value = "${uiState.completedTodos}",
                label = "已挽救",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                emoji = "🎯",
                value = "${uiState.totalTodos}",
                label = "行动中",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 分类分布
        Text(
            text = "遗憾分类分布",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkBlue)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val totalStats = uiState.categoryStats.sumOf { it.count }.coerceAtLeast(1)

                uiState.categoryStats.forEach { stat ->
                    val category = RegretCategory.fromName(stat.category)
                    val percentage = (stat.count * 100f / totalStats)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${category.emoji} ${category.displayName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary,
                            modifier = Modifier.width(80.dp)
                        )

                        // 进度条
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MidnightBlue)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(fraction = (percentage / 100f).coerceIn(0f, 1f))
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(category.getColor())
                            )
                        }

                        Text(
                            text = "${stat.count}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            modifier = Modifier
                                .width(36.dp)
                                .padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 最多共鸣 Top 10
        Text(
            text = "🔥 最多人共鸣的遗憾",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        uiState.topRegrets.forEachIndexed { index, regret ->
            val category = RegretCategory.fromName(regret.category)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DarkBlue)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // 排名
                    Text(
                        text = when (index) {
                            0 -> "🥇"
                            1 -> "🥈"
                            2 -> "🥉"
                            else -> "#${index + 1}"
                        },
                        fontSize = if (index < 3) 24.sp else 14.sp,
                        color = if (index >= 3) TextHint else TextPrimary,
                        modifier = Modifier
                            .width(36.dp)
                            .padding(top = if (index < 3) 0.dp else 4.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = regret.content,
                            style = MaterialTheme.typography.bodyMedium,
                            color = CandleGlow,
                            lineHeight = 22.sp
                        )
                        Row(
                            modifier = Modifier.padding(top = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${category.emoji} ${category.displayName}",
                                style = MaterialTheme.typography.labelSmall,
                                color = category.getColor()
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "❤ ${regret.resonateCount} 人共鸣",
                                style = MaterialTheme.typography.labelSmall,
                                color = ResonateColor
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 底部感悟
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MidnightBlue)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "💡", fontSize = 32.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "最大的遗憾不是做错了什么\n而是什么都没做",
                    style = MaterialTheme.typography.bodyLarge,
                    color = WarmAmber,
                    fontWeight = FontWeight.Medium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 26.sp
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    emoji: String,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkBlue)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = WarmAmber,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextHint
            )
        }
    }
}
