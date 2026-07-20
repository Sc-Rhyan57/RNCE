package com.rhyan57.rnce.model

import com.rhyan57.rnce.utils.IconName

enum class NfcType(val label: String) {
    URL("Link / URL"),
    URI("Generic URI"),
    TEXT("Plain Text"),
    CONTACT("Contact (vCard)"),
    WIFI("Wi-Fi Network"),
    LOCATION("Geo Location"),
    TELEGRAM("Telegram URI"),
    WHATSAPP("WhatsApp Link"),
}

data class PresetColor(
    val name: String,
    val hex: Long
) {
    companion object {
        val PRESETS = listOf(
            PresetColor("Violet", 0xFF7C3AED),
            PresetColor("Cyan", 0xFF06B6D4),
            PresetColor("Pink", 0xFFEC4899),
            PresetColor("Emerald", 0xFF10B981),
            PresetColor("Amber", 0xFFF59E0B),
            PresetColor("Rose", 0xFFF43F5E),
            PresetColor("Indigo", 0xFF6366F1),
            PresetColor("Sky", 0xFF0EA5E9),
            PresetColor("Orange", 0xFFF97316),
            PresetColor("Teal", 0xFF14B8A6),
        )
    }
}

data class NfcPreset(
    val id: String,
    val title: String,
    val description: String,
    val nfcType: NfcType,
    val iconName: IconName,
    val colorHex: Long,
    val createdAt: Long,
    val nfcData: NfcPresetData,
    val isActive: Boolean = false
)

sealed class NfcPresetData {
    data class UrlData(val url: String) : NfcPresetData()
    data class UriData(val uri: String) : NfcPresetData()
    data class TextData(val text: String) : NfcPresetData()
    data class ContactData(
        val firstName: String,
        val lastName: String = "",
        val phone: String = "",
        val email: String = "",
        val company: String = "",
        val title: String = "",
        val website: String = "",
        val notes: String = ""
    ) : NfcPresetData()
    data class WifiData(
        val ssid: String,
        val password: String = "",
        val isOpen: Boolean = false
    ) : NfcPresetData()
    data class LocationData(
        val latitude: Double,
        val longitude: Double
    ) : NfcPresetData()
    data class TelegramData(val usernameOrPhone: String) : NfcPresetData()
    data class WhatsAppData(val phone: String) : NfcPresetData()
}
