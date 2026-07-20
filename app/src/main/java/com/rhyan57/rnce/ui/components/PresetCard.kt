package com.rhyan57.rnce.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rhyan57.rnce.model.NfcPreset
import com.rhyan57.rnce.ui.theme.AppColors
import com.rhyan57.rnce.utils.toImageVector
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PresetCard(
    preset: NfcPreset,
    isActive: Boolean,
    showDescription: Boolean,
    onActivate: () -> Unit,
    onDeactivate: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val presetColor = Color(preset.colorHex)
    val dateStr = remember(preset.createdAt) {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(preset.createdAt))
    }
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        elevation = CardDefaults.cardElevation(if (isActive) 8.dp else 1.dp)
    ) {
        Box {
            if (isActive) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    presetColor.copy(alpha = 0f),
                                    presetColor,
                                    presetColor.copy(alpha = 0f)
                                )
                            )
                        )
                )
            }

            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isActive) {
                            val infiniteTransition = rememberInfiniteTransition(label = "ring")
                            val rotation by infiniteTransition.animateFloat(
                                0f, 360f,
                                infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart),
                                label = "rot"
                            )
                            Box(
                                Modifier
                                    .size(54.dp)
                                    .border(
                                        1.5.dp,
                                        Brush.sweepGradient(
                                            listOf(
                                                presetColor,
                                                presetColor.copy(0.05f),
                                                presetColor.copy(0.6f),
                                                presetColor
                                            )
                                        ),
                                        CircleShape
                                    )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(presetColor.copy(0.17f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                preset.iconName.toImageVector(), null,
                                tint = presetColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier
                                    .background(presetColor.copy(0.17f), RoundedCornerShape(5.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    preset.nfcType.label.uppercase(),
                                    fontSize = 8.5.sp, color = presetColor,
                                    fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp
                                )
                            }
                            if (isActive) {
                                Spacer(Modifier.width(6.dp))
                                Box(
                                    Modifier
                                        .background(AppColors.Success.copy(0.16f), RoundedCornerShape(5.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        "ACTIVE", fontSize = 8.5.sp,
                                        color = AppColors.Success, fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            preset.title,
                            fontSize = 15.sp, fontWeight = FontWeight.Bold,
                            color = AppColors.TextPrimary,
                            maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                        if (showDescription && preset.description.isNotBlank()) {
                            Text(
                                preset.description,
                                fontSize = 12.sp, color = AppColors.TextSecondary,
                                maxLines = 2, overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 2.dp), lineHeight = 16.sp
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(dateStr, fontSize = 10.sp, color = AppColors.TextMuted)
                    }

                    Spacer(Modifier.width(6.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box {
                            IconButton(onClick = { showMenu = true }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Outlined.MoreVert, null, tint = AppColors.TextMuted, modifier = Modifier.size(18.dp))
                            }
                            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                                DropdownMenuItem(
                                    text = { Text("Edit") },
                                    onClick = { showMenu = false; onEdit() },
                                    leadingIcon = { Icon(Icons.Outlined.Edit, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Delete", color = AppColors.Error) },
                                    onClick = { showMenu = false; onDelete() },
                                    leadingIcon = { Icon(Icons.Outlined.Delete, null, tint = AppColors.Error) }
                                )
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        if (isActive) {
                            IconButton(
                                onClick = onDeactivate,
                                modifier = Modifier.size(34.dp).background(AppColors.Error.copy(0.12f), CircleShape)
                            ) {
                                Icon(Icons.Outlined.Stop, null, tint = AppColors.Error, modifier = Modifier.size(17.dp))
                            }
                        } else {
                            IconButton(
                                onClick = onActivate,
                                modifier = Modifier.size(34.dp).background(presetColor.copy(0.14f), CircleShape)
                            ) {
                                Icon(Icons.Outlined.PlayArrow, null, tint = presetColor, modifier = Modifier.size(17.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
