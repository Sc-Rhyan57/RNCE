package com.rhyan57.rnce.model

import androidx.compose.runtime.mutableStateListOf

data class AppLog(
    val timestamp: String,
    val level: String,
    val tag: String,
    val message: String,
    val detail: String? = null
)

val appLogs = mutableStateListOf<AppLog>()

fun log(level: String, tag: String, message: String, detail: String? = null) {
    val ts = java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.getDefault())
        .format(java.util.Date())
    appLogs.add(0, AppLog(ts, level, tag, message, detail))
    if (appLogs.size > 200) appLogs.removeAt(appLogs.lastIndex)
}

fun logInfo(tag: String, msg: String, detail: String? = null) = log("INFO", tag, msg, detail)
fun logSuccess(tag: String, msg: String, detail: String? = null) = log("SUCCESS", tag, msg, detail)
fun logWarn(tag: String, msg: String, detail: String? = null) = log("WARN", tag, msg, detail)
fun logError(tag: String, msg: String, detail: String? = null) = log("ERROR", tag, msg, detail)
