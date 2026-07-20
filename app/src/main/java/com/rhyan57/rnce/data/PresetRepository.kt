package com.rhyan57.rnce.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rhyan57.rnce.model.NfcPreset
import com.rhyan57.rnce.utils.PresetSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "rnce_prefs")

class PresetRepository(private val context: Context) {

    companion object {
        private val KEY_PRESETS = stringPreferencesKey("presets")
        private val KEY_ACTIVE_PRESET_ID = stringPreferencesKey("active_preset_id")
        private val KEY_LOGS_ENABLED = booleanPreferencesKey("logs_enabled")
        private val KEY_SHOW_DESCRIPTIONS = booleanPreferencesKey("show_descriptions")
        private val KEY_HAPTIC_ENABLED = booleanPreferencesKey("haptic_enabled")
        private val KEY_AUTO_ACTIVATE = booleanPreferencesKey("auto_activate")
    }

    val presetsFlow: Flow<List<NfcPreset>> = context.dataStore.data.map { prefs ->
        val json = prefs[KEY_PRESETS] ?: return@map emptyList()
        PresetSerializer.fromJson(json)
    }

    val activePresetIdFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_ACTIVE_PRESET_ID]
    }

    val logsEnabledFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_LOGS_ENABLED] ?: true
    }

    val showDescriptionsFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_SHOW_DESCRIPTIONS] ?: true
    }

    val hapticEnabledFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_HAPTIC_ENABLED] ?: true
    }

    val autoActivateFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_AUTO_ACTIVATE] ?: false
    }

    suspend fun savePresets(presets: List<NfcPreset>) {
        context.dataStore.edit { prefs ->
            prefs[KEY_PRESETS] = PresetSerializer.toJson(presets)
        }
    }

    suspend fun setActivePresetId(id: String?) {
        context.dataStore.edit { prefs ->
            if (id != null) prefs[KEY_ACTIVE_PRESET_ID] = id
            else prefs.remove(KEY_ACTIVE_PRESET_ID)
        }
    }

    suspend fun setLogsEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LOGS_ENABLED] = enabled
        }
    }

    suspend fun setShowDescriptions(show: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SHOW_DESCRIPTIONS] = show
        }
    }

    suspend fun setHapticEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_HAPTIC_ENABLED] = enabled
        }
    }

    suspend fun setAutoActivate(auto: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_AUTO_ACTIVATE] = auto
        }
    }
}
