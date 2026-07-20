package com.rhyan57.rnce.ui.screens

import android.content.Intent
import android.net.Uri
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

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 110.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp).padding(top = 52.dp, bottom = 8.dp)) {
                Text("Settings", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Text("Preferences & configuration", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        item {
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

        item {
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

        item {
            SectionTitle("Behavior")
            SettingsCard {
                ToggleRow(Icons.Outlined.PlayCircle, "Auto-Activate Last",
                    "Re-activate last preset when app opens", autoActivate, vm::setAutoActivate)
            }
            Spacer(Modifier.height(14.dp))
        }

        item {
            SectionTitle("Developer")
            SettingsCard {
                ToggleRow(Icons.Outlined.Terminal, "App Logs",
                    "Enable in-app console (tap footer 5× to open)", logsEnabled, vm::setLogsEnabled)
            }
            Spacer(Modifier.height(14.dp))
        }

        item {
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

        item {
            SectionTitle("About")
            SettingsCard {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Nfc, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(30.dp))
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text("RNCE", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                        Text("NFC Card Emulator", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("v1.0 · by Rhyan57", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                CardDivider()
                Column(modifier = Modifier.padding(12.dp)) {
                    OutlinedButton(
                        onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Sc-Rhyan57"))) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
                    ) {
                        Icon(Icons.Outlined.Code, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("GitHub Profile")
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://dsc.gg/betterproject"))) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
                    ) {
                        Icon(Icons.Outlined.Forum, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Join Discord")
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
