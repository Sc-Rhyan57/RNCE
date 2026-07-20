package com.rhyan57.rnce.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rhyan57.rnce.hooks.MainViewModel
import com.rhyan57.rnce.ui.components.FooterBar
import com.rhyan57.rnce.ui.components.LogsScreen
import com.rhyan57.rnce.ui.theme.AppColors

@Composable
fun MainScreen(vm: MainViewModel) {
    var selectedTab       by remember { mutableIntStateOf(0) }
    var isFooterCollapsed by remember { mutableStateOf(false) }
    var footerClicks      by remember { mutableIntStateOf(0) }
    var showLogs          by remember { mutableStateOf(false) }
    var showCreate        by remember { mutableStateOf(false) }

    val logsEnabled by vm.logsEnabled.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(AppColors.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    0 -> HomeScreen(vm = vm, onScrollChanged = { isFooterCollapsed = it })
                    2 -> SettingsScreen(vm = vm)
                }
            }
            FooterBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    when (tab) {
                        1    -> { showCreate = true }
                        else -> { selectedTab = tab; isFooterCollapsed = false }
                    }
                },
                isCollapsed = isFooterCollapsed,
                footerClicks = footerClicks,
                onFooterClick = {
                    footerClicks++
                    if (footerClicks >= 5) { showLogs = true; footerClicks = 0 }
                }
            )
        }
    }

    if (showCreate) {
        CreatePresetScreen(
            editPreset = null,
            onSave = { preset -> vm.savePreset(preset); showCreate = false },
            onCancel = { showCreate = false }
        )
    }

    if (showLogs) {
        LogsScreen(
            onClose = { showLogs = false },
            logsEnabled = logsEnabled,
            onToggle = vm::setLogsEnabled
        )
    }
}
