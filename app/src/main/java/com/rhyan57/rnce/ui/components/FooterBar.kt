package com.rhyan57.rnce.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rhyan57.rnce.ui.theme.AppColors

@Composable
fun FooterBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    isCollapsed: Boolean,
    footerClicks: Int,
    onFooterClick: () -> Unit
) {
    val barHeight by animateDpAsState(
        targetValue = if (isCollapsed) 56.dp else 80.dp,
        animationSpec = tween(300),
        label = "bar_height"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AppColors.Background.copy(alpha = 0f),
                            AppColors.Surface.copy(alpha = 0.55f)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(AppColors.Divider.copy(alpha = 0.5f))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppColors.Surface)
                .height(barHeight)
                .navigationBarsPadding()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavTabItem(
                    icon = Icons.Outlined.Nfc,
                    label = "Presets",
                    selected = selectedTab == 0,
                    showLabel = !isCollapsed,
                    onClick = { onTabSelected(0) }
                )
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(AppColors.Primary, androidx.compose.foundation.shape.CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = { onTabSelected(1) }) {
                        Icon(
                            Icons.Outlined.Add, null,
                            tint = AppColors.OnPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                NavTabItem(
                    icon = Icons.Outlined.Settings,
                    label = "Settings",
                    selected = selectedTab == 2,
                    showLabel = !isCollapsed,
                    onClick = { onTabSelected(2) }
                )
            }

            AnimatedVisibility(
                visible = !isCollapsed,
                enter = expandVertically(tween(280)),
                exit = shrinkVertically(tween(220))
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Footer(clicks = footerClicks, onClick = onFooterClick)
                }
            }
        }
    }
}

@Composable
private fun NavTabItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    showLabel: Boolean,
    onClick: () -> Unit
) {
    val color = if (selected) AppColors.Primary else AppColors.TextMuted
    IconButton(onClick = onClick, modifier = Modifier.width(72.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = color, modifier = Modifier.size(22.dp))
            if (showLabel) {
                Spacer(Modifier.height(2.dp))
                Text(label, fontSize = 10.sp, color = color,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
            }
        }
    }
}
