package com.rhyan57.rnce.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rhyan57.rnce.hooks.MainViewModel

@Composable
fun SettingsScreen(vm: MainViewModel) {
    val context       = LocalContext.current
    val logsEnabled   by vm.logsEnabled.collectAsState()
    val showDesc      by vm.showDescriptions.collectAsState()
    val haptic        by vm.hapticEnabled.collectAsState()
    val autoActivate  by vm.autoActivate.collectAsState()
    val nfcEnabled    by vm.nfcEnabled.collectAsState()
    val nfcSupported  by vm.nfcSupported.collectAsState()

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 110.dp)
    ) {
        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(400)) + slideInVertically(tween(400), initialOffsetY = { -40 })
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp).padding(top = 52.dp, bottom = 8.dp)) {
                    Text("Settings", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    Text("Preferences & configuration", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500, delayMillis = 100)) + slideInVertically(tween(500), initialOffsetY = { 40 })
            ) {
                Column {
                    SectionTitle("NFC Status")
                    SettingsCard {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                if (nfcEnabled && nfcSupported) Icons.Outlined.Nfc else Icons.Outlined.WifiOff,
                                null,
                                tint = if (nfcEnabled && nfcSupported) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(Modifier.width(14.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    when {
                                        !nfcSupported -> "NFC not supported"
                                        nfcEnabled    -> "NFC is enabled"
                                        else          -> "NFC is disabled"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    when {
                                        !nfcSupported -> "This device has no NFC hardware"
                                        nfcEnabled    -> "Ready for NFC Type-4 HCE emulation"
                                        else          -> "Enable NFC to use emulation"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (!nfcEnabled && nfcSupported) {
                                TextButton(onClick = { vm.openNfcSettings() }) {
                                    Text("Enable", color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500, delayMillis = 200)) + slideInVertically(tween(500), initialOffsetY = { 40 })
            ) {
                Column {
                    SectionTitle("Interface")
                    SettingsCard {
                        ToggleRow(Icons.Outlined.Description, "Show Descriptions",
                            "Show preset description text in the list", showDesc, vm::setShowDescriptions)
                        CardDivider()
                        ToggleRow(Icons.Outlined.Vibration, "Haptic Feedback",
                            "Vibrate on preset interactions", haptic, vm::setHapticEnabled)
                    }
                    Spacer(Modifier.height(14.dp))
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500, delayMillis = 300)) + slideInVertically(tween(500), initialOffsetY = { 40 })
            ) {
                Column {
                    SectionTitle("Behavior")
                    SettingsCard {
                        ToggleRow(Icons.Outlined.PlayCircle, "Auto-Activate Last",
                            "Re-activate last preset when app opens", autoActivate, vm::setAutoActivate)
                    }
                    Spacer(Modifier.height(14.dp))
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500, delayMillis = 400)) + slideInVertically(tween(500), initialOffsetY = { 40 })
            ) {
                Column {
                    SectionTitle("Developer")
                    SettingsCard {
                        ToggleRow(Icons.Outlined.Terminal, "App Logs",
                            "Enable in-app console (tap footer 5× to open)", logsEnabled, vm::setLogsEnabled)
                    }
                    Spacer(Modifier.height(14.dp))
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500, delayMillis = 500)) + slideInVertically(tween(500), initialOffsetY = { 40 })
            ) {
                Column {
                    SectionTitle("NFC Types Reference")
                    SettingsCard {
                        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                            NfcTypeInfo(Icons.Outlined.Link,       "URL",       "Web link — works natively on Android & iOS")
                            NfcTypeInfo(Icons.Outlined.Language,   "URI",       "Generic deeplinks (app://, custom schemes)")
                            NfcTypeInfo(Icons.Outlined.TextFields, "Text",      "Plain text — needs a compatible NFC reader")
                            NfcTypeInfo(Icons.Outlined.Person,     "Contact",   "vCard 3.0 — native on Android")
                            NfcTypeInfo(Icons.Outlined.Wifi,       "Wi-Fi",     "WPA/WPA2 credentials — native on Android")
                            NfcTypeInfo(Icons.Outlined.LocationOn, "Location",  "geo: URI — opens Maps on Android & iOS")
                            NfcTypeInfo(Icons.Outlined.Send,       "Telegram",  "t.me/username or tg://msg link")
                            NfcTypeInfo(Icons.Outlined.Chat,       "WhatsApp",  "wa.me phone link")
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500, delayMillis = 600)) + slideInVertically(tween(500), initialOffsetY = { 40 })
            ) {
                Column {
                    SectionTitle("About")
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Nfc,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(84.dp)
                                )
                                Column {
                                    Text(
                                        text = "RNCE",
                                        style = MaterialTheme.typography.headlineLarge,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        letterSpacing = (-0.5).sp
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                        ) {
                                            Text(
                                                text = "v1.0",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                                        ) {
                                            Text(
                                                text = "by Rhyan57",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(Modifier.height(24.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                FilledTonalButton(
                                    onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Sc-Rhyan57"))) },
                                    modifier = Modifier.weight(1f).height(48.dp)
                                ) {
                                    Icon(Icons.Outlined.Person, contentDescription = null)
                                }
                                FilledTonalButton(
                                    onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Sc-Rhyan57/RNCE"))) },
                                    modifier = Modifier.weight(1f).height(48.dp)
                                ) {
                                    Icon(Icons.Outlined.Code, contentDescription = null)
                                }
                                FilledTonalButton(
                                    onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://dsc.gg/betterstar"))) },
                                    modifier = Modifier.weight(1f).height(48.dp)
                                ) {
                                    Icon(Icons.Outlined.Forum, contentDescription = null)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text.uppercase(), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 1.2.sp,
        modifier = Modifier.padding(start = 20.dp, bottom = 6.dp)
    )
}

@Composable
private fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) { Column(content = content) }
}

@Composable
private fun CardDivider() =
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(0.5f), modifier = Modifier.padding(horizontal = 16.dp))

@Composable
private fun ToggleRow(
    icon: ImageVector, title: String, subtitle: String,
    checked: Boolean, onToggle: (Boolean) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = checked, onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.surfaceVariant,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
private fun NfcTypeInfo(icon: ImageVector, name: String, description: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(17.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
