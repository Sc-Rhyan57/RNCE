package com.rhyan57.rnce

import android.app.Application
import com.rhyan57.rnce.model.logError

class RNCEApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupCrashHandler()
    }

    private fun setupCrashHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            val trace = buildString {
                append("Thread: ${thread.name}\n\n")
                append("${throwable::class.qualifiedName}: ${throwable.message}\n\n")
                throwable.stackTrace.forEach { append("  at $it\n") }
                var cause = throwable.cause
                while (cause != null) {
                    append("\nCaused by: ${cause::class.qualifiedName}: ${cause.message}\n")
                    cause.stackTrace.forEach { append("  at $it\n") }
                    cause = cause.cause
                }
            }
            logError("CRASH", throwable.message ?: "Unknown crash", trace)
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}
