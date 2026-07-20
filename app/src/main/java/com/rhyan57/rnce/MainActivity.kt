package com.rhyan57.rnce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.rhyan57.rnce.hooks.MainViewModel
import com.rhyan57.rnce.ui.screens.MainScreen
import com.rhyan57.rnce.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    private lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[MainViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MainScreen(vm = vm)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        vm.refreshNfcState()
    }
}
