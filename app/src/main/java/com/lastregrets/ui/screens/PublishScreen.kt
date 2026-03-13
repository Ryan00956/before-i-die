package com.lastregrets.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastregrets.data.model.RegretCategory
import com.lastregrets.ui.components.getColor
import com.lastregrets.ui.theme.*
import com.lastregrets.ui.viewmodel.PublishUiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PublishScreen(
    uiState: PublishUiState,
    onContentChange: (String) -> Unit,
    onCategorySelect: (RegretCategory) -> Unit,
    onSubmit: () -> Unit,
    onReset: () -> Unit
) {
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
            text = "✏️ 写下一条遗憾",
            style = MaterialTheme.typography.headlineLarge,
            color = WarmAmber,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "完全匿名，没有人知道你是谁",
            style = MaterialTheme.typography.bodySmall,
            color = TextHint,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        if (uiState.isSubmitted) {
            // 提交成功
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkBlue)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🙏", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "感谢你的分享",
                        style = MaterialTheme.typography.headlineMedium,
                        color = WarmAmber,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "你的遗憾也许会帮助某个人\n在还来得及的时候做出改变",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onReset,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WarmAmber,
                            contentColor = DeepNavy
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "再写一条",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            // 引导语
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MidnightBlue)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "💭 你可以写下：",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• 自己真实感受到的遗憾\n• 听长辈/身边人说过的遗憾\n• 书中读到的临终遗言",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 输入框
            OutlinedTextField(
                value = uiState.content,
                onValueChange = onContentChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                placeholder = {
                    Text(
                        text = "后悔没有……",
                        color = TextHint
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = WarmAmber,
                    unfocusedBorderColor = SlateBlue,
                    cursorColor = WarmAmber,
                    focusedTextColor = CandleGlow,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(16.dp),
                isError = uiState.errorMessage != null
            )

            // 错误消息
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = ErrorRed,
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                )
            }

            // 字数统计
            Text(
                text = "${uiState.content.length}/200",
                style = MaterialTheme.typography.labelSmall,
                color = TextHint,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 分类选择
            Text(
                text = "选择分类",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RegretCategory.entries.forEach { category ->
                    FilterChip(
                        selected = uiState.selectedCategory == category,
                        onClick = { onCategorySelect(category) },
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

            Spacer(modifier = Modifier.height(32.dp))

            // 提交按钮
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !uiState.isSubmitting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = WarmAmber,
                    contentColor = DeepNavy
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (uiState.isSubmitting) {
                    CircularProgressIndicator(
                        color = DeepNavy,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "匿名发布",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 隐私提示
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = TextHint,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "完全匿名发布，不会收集任何个人信息",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextHint
                )
            }
        }
    }
}
