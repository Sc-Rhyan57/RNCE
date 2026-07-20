package com.rhyan57.rnce

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
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {

    private lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupCrashHandler()

        val showCrash = intent.getBooleanExtra("show_crash", false)

        if (!showCrash) {
            vm = ViewModelProvider(this)[MainViewModel::class.java]
        }

        enableEdgeToEdge()
        setContent {
            AppTheme {
                if (showCrash && CrashHandler.trace != null) {
                    val trace = CrashHandler.trace!!
                    CrashHandler.trace = null
                    CrashScreen(trace = trace)
                } else {
                    MainScreen(vm = vm)
                }
            }
        }
    }

    private fun setupCrashHandler() {
        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            CrashHandler.trace = throwable.stackTraceToString()
            
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("show_crash", true)
            }
            startActivity(intent)
            
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(10)
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::vm.isInitialized) {
            vm.refreshNfcState()
        }
    }
}

object CrashHandler {
    var trace: String? = null
}
