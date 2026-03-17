package com.lastregrets.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.lastregrets.ui.theme.DarkBlue
import com.lastregrets.ui.theme.MidnightBlue
import com.lastregrets.ui.theme.SlateBlue

@Composable
fun shimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )

    return Brush.linearGradient(
        colors = listOf(MidnightBlue, SlateBlue.copy(alpha = 0.5f), MidnightBlue),
        start = Offset(translateAnim - 500f, translateAnim - 500f),
        end = Offset(translateAnim, translateAnim)
    )
}

@Composable
fun ShimmerRegretCard(modifier: Modifier = Modifier) {
    val brush = shimmerBrush()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkBlue)
            .padding(16.dp)
    ) {
        Box(Modifier.width(80.dp).height(14.dp).clip(RoundedCornerShape(4.dp)).background(brush))
        Spacer(Modifier.height(14.dp))
        Box(Modifier.fillMaxWidth().height(14.dp).clip(RoundedCornerShape(4.dp)).background(brush))
        Spacer(Modifier.height(8.dp))
        Box(Modifier.fillMaxWidth(0.7f).height(14.dp).clip(RoundedCornerShape(4.dp)).background(brush))
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(Modifier.width(90.dp).height(24.dp).clip(RoundedCornerShape(4.dp)).background(brush))
            Box(Modifier.width(90.dp).height(24.dp).clip(RoundedCornerShape(4.dp)).background(brush))
        }
    }
}

@Composable
fun ShimmerDailyCard(modifier: Modifier = Modifier) {
    val brush = shimmerBrush()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(DarkBlue)
            .padding(24.dp)
    ) {
        Box(Modifier.width(100.dp).height(16.dp).clip(RoundedCornerShape(4.dp)).background(brush))
        Spacer(Modifier.height(20.dp))
        Box(Modifier.fillMaxWidth().height(16.dp).clip(RoundedCornerShape(4.dp)).background(brush))
        Spacer(Modifier.height(10.dp))
        Box(Modifier.fillMaxWidth(0.85f).height(16.dp).clip(RoundedCornerShape(4.dp)).background(brush))
        Spacer(Modifier.height(10.dp))
        Box(Modifier.fillMaxWidth(0.5f).height(16.dp).clip(RoundedCornerShape(4.dp)).background(brush))
        Spacer(Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(Modifier.width(110.dp).height(32.dp).clip(RoundedCornerShape(8.dp)).background(brush))
            Box(Modifier.width(110.dp).height(32.dp).clip(RoundedCornerShape(12.dp)).background(brush))
        }
    }
}
