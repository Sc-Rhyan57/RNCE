package com.rhyan57.rnce.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rhyan57.rnce.ui.theme.AppColors

@Composable
fun Footer(clicks: Int, onClick: () -> Unit) {
    val t = rememberInfiniteTransition(label = "rgb")
    val hue by t.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(3000, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "h"
    )
    val c = Color.hsv(hue, 0.75f, 1f)
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            Modifier.clickable { onClick() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.AutoAwesome, null, tint = c, modifier = Modifier.size(13.dp))
            Spacer(Modifier.width(6.dp))
            Text("By Rhyan57", fontSize = 12.sp, color = c, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(6.dp))
            Icon(Icons.Outlined.AutoAwesome, null, tint = c, modifier = Modifier.size(13.dp))
        }
        if (clicks in 1..4) {
            Text(
                "${5 - clicks}x more to open console",
                fontSize = 10.sp,
                color = AppColors.TextMuted.copy(0.5f)
            )
        }
    }
}
