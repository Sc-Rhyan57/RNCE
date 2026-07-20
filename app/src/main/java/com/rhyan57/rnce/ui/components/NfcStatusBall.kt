package com.rhyan57.rnce.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Nfc
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rhyan57.rnce.ui.theme.AppColors

@Composable
fun NfcStatusBall(
    isActive: Boolean,
    isEmulating: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 140.dp
) {
    val color by animateColorAsState(
        targetValue = when {
            !isActive -> AppColors.NfcInactive
            isEmulating -> AppColors.NfcActive
            else -> AppColors.NfcActive.copy(alpha = 0.6f)
        },
        animationSpec = tween(600),
        label = "nfc_color"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "waves")

    val wave1 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2200, easing = LinearEasing), RepeatMode.Restart
        ), label = "w1"
    )
    val wave2 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2200, easing = LinearEasing, delayMillis = 700), RepeatMode.Restart
        ), label = "w2"
    )
    val wave3 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2200, easing = LinearEasing, delayMillis = 1400), RepeatMode.Restart
        ), label = "w3"
    )

    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = this.size.width / 2f
            val cy = this.size.height / 2f
            val base = this.size.minDimension / 2f

            if (isActive && isEmulating) {
                listOf(wave1, wave2, wave3).forEach { p ->
                    val radius = base * (0.42f + p * 1.0f)
                    val alpha = (1f - p).coerceIn(0f, 1f) * 0.38f
                    drawCircle(
                        color = color.copy(alpha = alpha),
                        radius = radius,
                        center = Offset(cx, cy),
                        style = Stroke(width = 2.5.dp.toPx())
                    )
                }
                for (i in 5 downTo 1) {
                    drawCircle(
                        color = color.copy(alpha = 0.055f * i),
                        radius = base * 0.46f + i * 3.5.dp.toPx(),
                        center = Offset(cx, cy)
                    )
                }
            } else {
                drawCircle(
                    color = color.copy(alpha = 0.10f),
                    radius = base * 0.54f,
                    center = Offset(cx, cy)
                )
            }

            drawCircle(
                color = color.copy(alpha = 0.22f),
                radius = base * 0.44f,
                center = Offset(cx, cy)
            )
            drawCircle(
                color = color,
                radius = base * 0.34f,
                center = Offset(cx, cy)
            )
        }

        Icon(
            imageVector = Icons.Outlined.Nfc,
            contentDescription = "NFC",
            tint = Color.White,
            modifier = Modifier.size(size * 0.30f)
        )
    }
}
