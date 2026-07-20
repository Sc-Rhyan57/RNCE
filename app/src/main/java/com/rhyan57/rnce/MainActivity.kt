package com.rhyan57.rnce

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.rhyan57.rnce.hooks.MainViewModel
import com.rhyan57.rnce.ui.components.CrashScreen
import com.rhyan57.rnce.ui.screens.MainScreen
import com.rhyan57.rnce.ui.theme.AppTheme
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {

    companion object {
        private const val PREF_CRASH = "crash_prefs"
        private const val KEY_CRASH_TRACE = "crash_trace"
    }

    private lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupCrashHandler()

        val crashPrefs = getSharedPreferences(PREF_CRASH, Context.MODE_PRIVATE)
        val crashTrace = crashPrefs.getString(KEY_CRASH_TRACE, null)
        if (crashTrace != null) {
            crashPrefs.edit().remove(KEY_CRASH_TRACE).apply()
        }

        if (crashTrace == null) {
            vm = ViewModelProvider(this)[MainViewModel::class.java]
        }

        enableEdgeToEdge()
        setContent {
            AppTheme {
                if (crashTrace != null) {
                    CrashScreen(trace = crashTrace)
                } else {
                    MainScreen(vm = vm)
                }
            }
        }
    }

    private fun setupCrashHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            try {
                val sw = StringWriter()
                e.printStackTrace(PrintWriter(sw))
                val log = "SEND THIS TO Sc-rhyan57 ON GITHUB!! \nRNCE - Crash Report\nManufacturer: ${android.os.Build.MANUFACTURER}\nDevice: ${android.os.Build.MODEL}\nAndroid: ${android.os.Build.VERSION.RELEASE}\nStacktrace:\n$sw"
                
                getSharedPreferences(PREF_CRASH, Context.MODE_PRIVATE)
                    .edit()
                    .putString(KEY_CRASH_TRACE, log)
                    .commit()
                    
                startActivity(Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                })
            } catch (_: Exception) {
                defaultHandler?.uncaughtException(t, e)
            }
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(2)
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::vm.isInitialized) {
            vm.refreshNfcState()
        }
    }
}
