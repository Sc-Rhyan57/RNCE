package com.rhyan57.rnce.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rhyan57.rnce.ui.theme.AppColors
import com.rhyan57.rnce.ui.theme.Radius

@Composable
fun CrashScreen(trace: String) {
    val ctx = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().background(AppColors.Background)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Icon(Icons.Outlined.Warning, null, tint = AppColors.Error, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(10.dp))
                Text(
                    "App Crashed",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = AppColors.TextPrimary
                )
            }
            TextButton(onClick = { android.os.Process.killProcess(android.os.Process.myPid()) }) {
                Text("Close", color = AppColors.Error, fontWeight = FontWeight.Bold)
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).padding(bottom = 16.dp)) {
            Text(
                "An unexpected error occurred. Copy and report to Sc-Rhyan57.",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.TextSecondary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Button(
                onClick = {
                    val cm = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cm.setPrimaryClip(ClipData.newPlainText("Crash Log", trace))
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary),
                shape = Radius.Button
            ) {
                Icon(Icons.Outlined.ContentCopy, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("Copy Log", fontWeight = FontWeight.Bold, color = AppColors.OnPrimary)
            }

            OutlinedButton(
                onClick = {
                    ctx.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Sc-Rhyan57/RNCE"))
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.TextMuted),
                border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.Divider),
                shape = Radius.Button
            ) {
                Icon(Icons.Outlined.Code, null, modifier = Modifier.size(16.dp), tint = AppColors.TextMuted)
                Spacer(Modifier.width(8.dp))
                Text("Sc-Rhyan57/RNCE", fontFamily = FontFamily.Monospace, fontSize = 13.sp)
            }

            Card(
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = AppColors.ErrorContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                LazyColumn(Modifier.fillMaxSize()) {
                    item {
                        Text(
                            trace, modifier = Modifier.padding(14.dp),
                            fontFamily = FontFamily.Monospace, fontSize = 12.sp,
                            color = AppColors.OnError, lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
