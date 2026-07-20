package com.rhyan57.rnce.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rhyan57.rnce.hooks.MainViewModel
import com.rhyan57.rnce.model.NfcPreset
import com.rhyan57.rnce.ui.components.NfcStatusBall
import com.rhyan57.rnce.ui.components.PresetCard
import com.rhyan57.rnce.ui.theme.AppColors
import com.rhyan57.rnce.ui.theme.Radius

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
        modifier = Modifier.fillMaxSize().background(AppColors.Background),
        contentPadding = PaddingValues(bottom = 110.dp)
    ) {
        item {
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
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AppColors.TextPrimary,
                            letterSpacing = 2.sp
                        )
                        Text("NFC Card Emulator", fontSize = 12.sp, color = AppColors.TextMuted)
                    }
                    val statusOk = nfcEnabled && nfcSupported
                    Box(
                        modifier = Modifier
                            .background(
                                if (isEmulating) AppColors.Primary.copy(0.15f)
                                else if (statusOk) AppColors.Success.copy(0.12f)
                                else AppColors.Error.copy(0.12f),
                                Radius.Badge
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (statusOk) Icons.Outlined.Nfc else Icons.Outlined.WifiOff,
                                null,
                                tint = if (isEmulating) AppColors.Primary
                                       else if (statusOk) AppColors.Success
                                       else AppColors.Error,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(
                                when {
                                    !nfcSupported -> "Not supported"
                                    !nfcEnabled   -> "NFC off"
                                    isEmulating   -> "Emulating"
                                    else          -> "Ready"
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isEmulating) AppColors.Primary
                                        else if (statusOk) AppColors.Success
                                        else AppColors.Error
                            )
                        }
                    }
                }

                Spacer(Modifier.height(28.dp))

                NfcStatusBall(
                    isActive = nfcEnabled && nfcSupported,
                    isEmulating = isEmulating,
                    size = 150.dp
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    when {
                        !nfcSupported   -> "NFC not supported on this device"
                        !nfcEnabled     -> "NFC is disabled — tap below to enable"
                        isEmulating     -> "Emulation active ✦"
                        presets.isEmpty() -> "No presets yet — tap + to create one"
                        else            -> "Select a preset to start emulating"
                    },
                    fontSize = 13.sp,
                    color = if (isEmulating) AppColors.Primary else AppColors.TextMuted
                )

                if (!nfcEnabled && nfcSupported) {
                    Spacer(Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { vm.openNfcSettings() },
                        shape = Radius.Button,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.Warning),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.Warning.copy(0.4f))
                    ) {
                        Icon(Icons.Outlined.Settings, null, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Enable NFC", fontSize = 12.sp)
                    }
                }

                if (isEmulating) {
                    Spacer(Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { vm.deactivate() },
                        shape = Radius.Button,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.Error),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.Error.copy(0.35f))
                    ) {
                        Icon(Icons.Outlined.Stop, null, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Stop Emulation", fontSize = 12.sp)
                    }
                }

                Spacer(Modifier.height(20.dp))
                HorizontalDivider(
                    color = AppColors.Divider,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }

        if (presets.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Outlined.Nfc, null,
                            tint = AppColors.TextMuted.copy(0.4f),
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(Modifier.height(14.dp))
                        Text(
                            "No presets yet",
                            color = AppColors.TextMuted,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Tap the + button below to create your first",
                            color = AppColors.TextMuted.copy(0.6f),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        } else {
            item {
                Text(
                    "YOUR PRESETS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextMuted,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 4.dp)
                )
            }
            items(presets, key = { it.id }) { preset ->
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

    if (deleteConfirmId != null) {
        AlertDialog(
            onDismissRequest = { deleteConfirmId = null },
            title = { Text("Delete Preset") },
            text = { Text("Are you sure? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    deleteConfirmId?.let { vm.deletePreset(it) }
                    deleteConfirmId = null
                }) { Text("Delete", color = AppColors.Error, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { deleteConfirmId = null }) { Text("Cancel") }
            },
            containerColor = AppColors.Surface,
            titleContentColor = AppColors.TextPrimary,
            textContentColor = AppColors.TextSecondary
        )
    }
}
