@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.rhyan57.rnce.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rhyan57.rnce.hooks.MainViewModel
import com.rhyan57.rnce.model.NfcPreset
import com.rhyan57.rnce.ui.components.PresetCard

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
                enter = fadeIn(tween(1000)) + slideInVertically(tween(1000), initialOffsetY = { -40 })
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
                                "RNCE by rhyan57",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onBackground,
                                letterSpacing = 0.sp
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

                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(180.dp)) {
                        if (isEmulating) {
                            CircularWavyProgressIndicator(
                                modifier = Modifier.size(140.dp),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            )
                        } else {
                            CircularWavyProgressIndicator(
                                progress = { 1f },
                                modifier = Modifier.size(140.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            )
                        }
                        
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isEmulating) Icons.Outlined.WifiTethering else Icons.Outlined.Nfc,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
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
                    enter = fadeIn(tween(1200, delayMillis = 300))
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Outlined.Nfc, null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.size(56.dp)
                            )
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
                    enter = fadeIn(tween(1000, delayMillis = 200))
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
                    enter = fadeIn(tween(800, delayMillis = 200 + index * 80)) + slideInVertically(tween(800), initialOffsetY = { 40 })
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
