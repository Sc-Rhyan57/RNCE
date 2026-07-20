package com.rhyan57.rnce.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rhyan57.rnce.model.AppLog
import com.rhyan57.rnce.model.appLogs
import com.rhyan57.rnce.ui.theme.AppColors

@Composable
fun LogsScreen(
    onClose: () -> Unit,
    logsEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(modifier = Modifier.fillMaxSize().background(AppColors.Background)) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.Surface)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Terminal, null,
                            tint = AppColors.Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "App Console",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = AppColors.TextPrimary
                        )
                        Spacer(Modifier.width(8.dp))
                        Box(
                            Modifier
                                .background(AppColors.Primary.copy(0.15f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "${appLogs.size}",
                                fontSize = 10.sp,
                                color = AppColors.Primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Logging", fontSize = 11.sp, color = AppColors.TextMuted)
                        Spacer(Modifier.width(6.dp))
                        Switch(
                            checked = logsEnabled,
                            onCheckedChange = onToggle,
                            modifier = Modifier.height(24.dp),
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = AppColors.Primary,
                                checkedTrackColor = AppColors.Primary.copy(0.3f)
                            )
                        )
                        Spacer(Modifier.width(8.dp))
                        TextButton(onClick = { appLogs.clear() }) {
                            Text("Clear", color = AppColors.Error, fontSize = 11.sp)
                        }
                        IconButton(onClick = onClose, modifier = Modifier.size(32.dp)) {
                            Icon(
                                Icons.Outlined.Close, null,
                                tint = AppColors.TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                if (appLogs.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Outlined.Terminal, null,
                                tint = AppColors.TextMuted,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text("No logs yet", color = AppColors.TextMuted, fontSize = 14.sp)
                        }
                    }
                } else {
                    LazyColumn(Modifier.fillMaxSize().padding(8.dp)) {
                        items(appLogs) { log ->
                            LogEntry(log)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LogEntry(log: AppLog) {
    var expanded by remember { mutableStateOf(false) }
    val levelColor = when (log.level) {
        "SUCCESS" -> AppColors.Success
        "ERROR" -> AppColors.Error
        "WARN" -> AppColors.Warning
        else -> AppColors.Primary
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .background(AppColors.Surface.copy(0.5f), RoundedCornerShape(8.dp))
            .clickable { expanded = !expanded }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                log.timestamp,
                fontSize = 10.sp,
                color = AppColors.TextMuted,
                fontFamily = FontFamily.Monospace
            )
            Spacer(Modifier.width(5.dp))
            Box(
                Modifier
                    .background(levelColor.copy(0.15f), RoundedCornerShape(3.dp))
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Text(
                    log.level,
                    fontSize = 9.sp,
                    color = levelColor,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
            Spacer(Modifier.width(5.dp))
            Text(
                "[${log.tag}]",
                fontSize = 10.sp,
                color = AppColors.Primary,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(4.dp))
            Text(
                log.message,
                fontSize = 11.sp,
                color = AppColors.TextSecondary,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.weight(1f),
                maxLines = if (expanded) Int.MAX_VALUE else 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                null,
                tint = AppColors.TextMuted,
                modifier = Modifier.size(14.dp)
            )
        }
        if (expanded && log.detail != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                log.detail,
                fontSize = 10.sp,
                color = AppColors.TextMuted,
                fontFamily = FontFamily.Monospace,
                lineHeight = 14.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}
