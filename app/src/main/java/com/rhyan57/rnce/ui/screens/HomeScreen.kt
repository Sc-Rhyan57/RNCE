package com.rhyan57.rnce.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rhyan57.rnce.hooks.MainViewModel
import com.rhyan57.rnce.model.NfcPreset
import com.rhyan57.rnce.ui.components.PresetCard
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomeScreen(
    vm: MainViewModel,
    onScrollChanged: (Boolean) -> Unit
) {
    val presets        by vm.presets.collectAsState()
    val activePresetId by vm.activePresetId.collectAsState()
    val nfcEnabled     by vm.nfcEnabled.collectAsState()
    val nfcSupported   by vm.nfcSupported.collectAsState()
    val isEmulating    by vm.isEmulating.collectAsState()
    val showDesc       by vm.showDescriptions.collectAsState()

    var editingPreset   by remember { mutableStateOf<NfcPreset?>(null) }
    var deleteConfirmId by remember { mutableStateOf<String?>(null) }

    val listState = rememberLazyListState()
    val isScrolled by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 30
        }
    }
    LaunchedEffect(isScrolled) { onScrollChanged(isScrolled) }

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    if (editingPreset != null) {
        CreatePresetScreen(
            editPreset = editingPreset,
            onSave = { p -> vm.savePreset(p); editingPreset = null },
            onCancel = { editingPreset = null }
        )
        return
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 110.dp)
    ) {
        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500)) + slideInVertically(tween(500), initialOffsetY = { -40 })
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 48.dp, bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "RNCE",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onBackground,
                                letterSpacing = 2.sp
                            )
                            Text(
                                "NFC Card Emulator",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        val statusOk = nfcEnabled && nfcSupported
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = when {
                                isEmulating -> MaterialTheme.colorScheme.primary.copy(0.15f)
                                statusOk -> MaterialTheme.colorScheme.tertiary.copy(0.15f)
                                else -> MaterialTheme.colorScheme.error.copy(0.15f)
                            },
                            contentColor = when {
                                isEmulating -> MaterialTheme.colorScheme.primary
                                statusOk -> MaterialTheme.colorScheme.tertiary
                                else -> MaterialTheme.colorScheme.error
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    if (statusOk) Icons.Outlined.Nfc else Icons.Outlined.WifiOff,
                                    null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    when {
                                        !nfcSupported -> "Not supported"
                                        !nfcEnabled   -> "NFC off"
                                        isEmulating   -> "Emulating"
                                        else          -> "Ready"
                                    },
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(28.dp))

                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(160.dp)) {
                        SpiralWaveLoader(
                            color = MaterialTheme.colorScheme.primary,
                            isEmulating = isEmulating
                        )
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(0.9f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isEmulating) "📡" else "💳",
                                fontSize = 36.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Text(
                        when {
                            !nfcSupported   -> "NFC not supported on this device"
                            !nfcEnabled     -> "NFC is disabled — tap below to enable"
                            isEmulating     -> "Emulation active ✦"
                            presets.isEmpty() -> "No presets yet — tap + to create one"
                            else            -> "Select a preset to start emulating"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isEmulating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (!nfcEnabled && nfcSupported) {
                        Spacer(Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = { vm.openNfcSettings() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.secondary),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(0.4f))
                        ) {
                            Icon(Icons.Outlined.Settings, null, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Enable NFC")
                        }
                    }

                    if (isEmulating) {
                        Spacer(Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = { vm.deactivate() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(0.35f))
                        ) {
                            Icon(Icons.Outlined.Stop, null, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Stop Emulation")
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }

        if (presets.isEmpty()) {
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(800, delayMillis = 200))
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("💳", fontSize = 48.sp)
                            Spacer(Modifier.height(14.dp))
                            Text(
                                "No presets yet",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Tap the + button below to create your first",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        } else {
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(500, delayMillis = 100))
                ) {
                    Text(
                        "YOUR PRESETS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.2.sp,
                        modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 4.dp)
                    )
                }
            }
            itemsIndexed(presets, key = { _, it -> it.id }) { index, preset ->
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(400, delayMillis = 150 + index * 50)) + slideInVertically(tween(400), initialOffsetY = { 40 })
                ) {
                    PresetCard(
                        preset = preset,
                        isActive = preset.id == activePresetId,
                        showDescription = showDesc,
                        onActivate = { vm.activatePreset(preset) },
                        onDeactivate = { vm.deactivate() },
                        onEdit = { editingPreset = preset },
                        onDelete = { deleteConfirmId = preset.id }
                    )
                }
            }
        }
    }

    if (deleteConfirmId != null) {
        AlertDialog(
            onDismissRequest = { deleteConfirmId = null },
            title = { Text("Delete Preset") },
            text = { Text("Are you sure? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    deleteConfirmId?.let { vm.deletePreset(it) }
                    deleteConfirmId = null
                }) { Text("Delete", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { deleteConfirmId = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun SpiralWaveLoader(
    modifier: Modifier = Modifier,
    color: Color,
    isEmulating: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spiral")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Restart),
        label = "rotation"
    )
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(if (isEmulating) 500 else 1500, easing = LinearEasing), RepeatMode.Restart),
        label = "wave"
    )

    Canvas(modifier = modifier.size(160.dp)) {
        val center = Offset(this.size.width / 2f, this.size.height / 2f)
        val baseRadius = this.size.minDimension / 3.2f
        val amplitude = if (isEmulating) 16f else 6f
        val layers = 3

        for (layer in 0 until layers) {
            val path = Path()
            val steps = 180
            val layerRadius = baseRadius - (layer * 22f)
            val waveCount = 6 + layer * 2
            val layerRotation = if (layer % 2 == 0) rotation else -rotation * 1.5f

            for (i in 0..steps) {
                val angle = (i.toFloat() / steps) * 2 * PI
                val wave = sin(angle * waveCount + wavePhase + layer).toFloat() * amplitude
                val r = layerRadius + wave
                val x = center.x + cos(angle).toFloat() * r
                val y = center.y + sin(angle).toFloat() * r
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()

            rotate(degrees = layerRotation, pivot = center) {
                drawPath(
                    path = path,
                    color = color.copy(alpha = 0.9f - (layer * 0.25f)),
                    style = Stroke(width = 3.5f, cap = StrokeCap.Round)
                )
            }
        }
    }
}
