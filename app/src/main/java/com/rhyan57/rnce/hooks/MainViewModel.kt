package com.rhyan57.rnce.hooks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rhyan57.rnce.data.PresetRepository
import com.rhyan57.rnce.model.NfcPreset
import com.rhyan57.rnce.model.logInfo
import com.rhyan57.rnce.nfc.NfcController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repo    = PresetRepository(application)
    private val context get() = getApplication<Application>().applicationContext

    val presets: StateFlow<List<NfcPreset>> = repo.presetsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val activePresetId: StateFlow<String?> = repo.activePresetIdFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val logsEnabled: StateFlow<Boolean> = repo.logsEnabledFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

    val showDescriptions: StateFlow<Boolean> = repo.showDescriptionsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

    val hapticEnabled: StateFlow<Boolean> = repo.hapticEnabledFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

    val autoActivate: StateFlow<Boolean> = repo.autoActivateFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    private val _nfcSupported = MutableStateFlow(NfcController.isNfcSupported(context))
    val nfcSupported: StateFlow<Boolean> = _nfcSupported.asStateFlow()

    private val _nfcEnabled = MutableStateFlow(NfcController.isNfcEnabled(context))
    val nfcEnabled: StateFlow<Boolean> = _nfcEnabled.asStateFlow()

    private val _isEmulating = MutableStateFlow(NfcController.hasActiveEmulation(context))
    val isEmulating: StateFlow<Boolean> = _isEmulating.asStateFlow()

    fun refreshNfcState() {
        _nfcEnabled.value  = NfcController.isNfcEnabled(context)
        _isEmulating.value = NfcController.hasActiveEmulation(context)
    }

    fun activatePreset(preset: NfcPreset) {
        viewModelScope.launch {
            val ok = NfcController.activatePreset(context, preset)
            if (ok) {
                repo.setActivePresetId(preset.id)
                _isEmulating.value = true
            }
        }
    }

    fun deactivate() {
        viewModelScope.launch {
            NfcController.deactivate(context)
            repo.setActivePresetId(null)
            _isEmulating.value = false
        }
    }

    fun savePreset(preset: NfcPreset) {
        viewModelScope.launch {
            val list = presets.value.toMutableList()
            val idx  = list.indexOfFirst { it.id == preset.id }
            if (idx >= 0) list[idx] = preset else list.add(0, preset)
            repo.savePresets(list)
            logInfo("PRESET", "Saved '${preset.title}'")
        }
    }

    fun deletePreset(id: String) {
        viewModelScope.launch {
            val list = presets.value.toMutableList()
            list.removeAll { it.id == id }
            repo.savePresets(list)
            if (activePresetId.value == id) {
                NfcController.deactivate(context)
                repo.setActivePresetId(null)
                _isEmulating.value = false
            }
            logInfo("PRESET", "Deleted preset $id")
        }
    }

    fun setLogsEnabled(v: Boolean)       = viewModelScope.launch { repo.setLogsEnabled(v) }
    fun setShowDescriptions(v: Boolean)  = viewModelScope.launch { repo.setShowDescriptions(v) }
    fun setHapticEnabled(v: Boolean)     = viewModelScope.launch { repo.setHapticEnabled(v) }
    fun setAutoActivate(v: Boolean)      = viewModelScope.launch { repo.setAutoActivate(v) }

    fun openNfcSettings() {
        val intent = android.content.Intent(android.provider.Settings.ACTION_NFC_SETTINGS)
            .addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
