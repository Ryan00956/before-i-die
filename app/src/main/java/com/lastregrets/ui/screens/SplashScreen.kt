package com.lastregrets.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastregrets.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    val candleAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "candleAlpha"
    )

    val candleScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "candleScale"
    )

    val titleAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(800, delayMillis = 600, easing = FastOutSlowInEasing),
        label = "titleAlpha"
    )

    val subtitleAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(800, delayMillis = 1100, easing = FastOutSlowInEasing),
        label = "subtitleAlpha"
    )

    // 烛光呼吸效果
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavy),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "🕯️",
                fontSize = 80.sp,
                modifier = Modifier
                    .alpha(candleAlpha)
                    .scale(candleScale * glowScale)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "临终清单",
                style = MaterialTheme.typography.displayLarge,
                color = WarmAmber,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(titleAlpha)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "别等死了才后悔",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.alpha(subtitleAlpha)
            )
        }
    }
}
