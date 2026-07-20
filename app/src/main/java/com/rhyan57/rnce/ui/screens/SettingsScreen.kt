package com.rhyan57.rnce.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.rhyan57.rnce.ui.theme.AppColors
import com.rhyan57.rnce.ui.theme.Radius

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
        modifier = Modifier.fillMaxSize().background(AppColors.Background),
        contentPadding = PaddingValues(bottom = 110.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp).padding(top = 52.dp, bottom = 8.dp)) {
                Text("Settings", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = AppColors.TextPrimary)
                Text("Preferences & configuration", fontSize = 13.sp, color = AppColors.TextMuted)
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
                        tint = if (nfcEnabled && nfcSupported) AppColors.Success else AppColors.Error,
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
                            fontWeight = FontWeight.SemiBold,
                            color = AppColors.TextPrimary
                        )
                        Text(
                            when {
                                !nfcSupported -> "This device has no NFC hardware"
                                nfcEnabled    -> "Ready for NFC Type-4 HCE emulation"
                                else          -> "Enable NFC to use emulation"
                            },
                            fontSize = 12.sp,
                            color = AppColors.TextMuted
                        )
                    }
                    if (!nfcEnabled && nfcSupported) {
                        TextButton(onClick = { vm.openNfcSettings() }) {
                            Text("Enable", color = AppColors.Primary, fontSize = 12.sp)
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
                    Icon(Icons.Outlined.Nfc, null, tint = AppColors.Primary, modifier = Modifier.size(30.dp))
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text("RNCE", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = AppColors.TextPrimary)
                        Text("NFC Card Emulator", fontSize = 12.sp, color = AppColors.TextSecondary)
                        Text("v1.0 · by Rhyan57", fontSize = 11.sp, color = AppColors.TextMuted)
                    }
                }
                CardDivider()
                OutlinedButton(
                    onClick = {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Sc-Rhyan57/GetDiscordToken"))
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.TextMuted),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.Divider),
                    shape = Radius.Button
                ) {
                    Icon(Icons.Outlined.Code, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Sc-Rhyan57 on GitHub", fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold,
        color = AppColors.TextMuted, letterSpacing = 1.2.sp,
        modifier = Modifier.padding(start = 20.dp, bottom = 6.dp)
    )
}

@Composable
private fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        shape = RoundedCornerShape(16.dp)
    ) { Column(content = content) }
}

@Composable
private fun CardDivider() =
    HorizontalDivider(color = AppColors.Divider.copy(0.45f), modifier = Modifier.padding(horizontal = 16.dp))

@Composable
private fun ToggleRow(
    icon: ImageVector, title: String, subtitle: String,
    checked: Boolean, onToggle: (Boolean) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = AppColors.Primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, color = AppColors.TextPrimary, fontSize = 14.sp)
            Text(subtitle, fontSize = 12.sp, color = AppColors.TextMuted, lineHeight = 16.sp)
        }
        Switch(
            checked = checked, onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = AppColors.Primary,
                checkedTrackColor = AppColors.Primary.copy(0.3f)
            )
        )
    }
}

@Composable
private fun NfcTypeInfo(icon: ImageVector, name: String, description: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = AppColors.Primary, modifier = Modifier.size(17.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = AppColors.TextPrimary)
            Text(description, fontSize = 11.sp, color = AppColors.TextMuted)
        }
    }
}
