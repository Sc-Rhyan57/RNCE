package com.rhyan57.rnce.nfc

import android.content.Context
import android.nfc.NfcAdapter
import com.luigivampa92.ndefemulation.NdefEmulation
import com.luigivampa92.ndefemulation.ndef.ContactNdefData
import com.luigivampa92.ndefemulation.ndef.LocationNdefData
import com.luigivampa92.ndefemulation.ndef.TextNdefData
import com.luigivampa92.ndefemulation.ndef.UriNdefData
import com.luigivampa92.ndefemulation.ndef.WifiNetworkNdefData
import com.luigivampa92.ndefemulation.ndef.WifiNetworkNdefDataProtectionType
import com.rhyan57.rnce.model.NfcPreset
import com.rhyan57.rnce.model.NfcPresetData
import com.rhyan57.rnce.model.logError
import com.rhyan57.rnce.model.logInfo
import com.rhyan57.rnce.model.logSuccess
import com.rhyan57.rnce.model.logWarn

object NfcController {

    fun isNfcSupported(context: Context): Boolean {
        return NfcAdapter.getDefaultAdapter(context) != null
    }

    fun isNfcEnabled(context: Context): Boolean {
        return NfcAdapter.getDefaultAdapter(context)?.isEnabled == true
    }

    fun activatePreset(context: Context, preset: NfcPreset): Boolean {
        return try {
            val emulation = NdefEmulation(context)
            val ndefData = when (val data = preset.nfcData) {
                is NfcPresetData.UrlData -> {
                    logInfo("NFC", "Activating URL preset", data.url)
                    UriNdefData(data.url)
                }
                is NfcPresetData.UriData -> {
                    logInfo("NFC", "Activating URI preset", data.uri)
                    UriNdefData(data.uri)
                }
                is NfcPresetData.TextData -> {
                    logInfo("NFC", "Activating Text preset", data.text)
                    TextNdefData(data.text)
                }
                is NfcPresetData.ContactData -> {
                    logInfo("NFC", "Activating Contact preset", "${data.firstName} ${data.lastName}")
                    ContactNdefData(
                        firstName = data.firstName,
                        lastName = data.lastName.ifBlank { null },
                        phoneNumber = data.phone.ifBlank { null },
                        email = data.email.ifBlank { null },
                        jobCompany = data.company.ifBlank { null },
                        jobTitle = data.title.ifBlank { null },
                        siteUrl = data.website.ifBlank { null },
                        notes = data.notes.ifBlank { null }
                    )
                }
                is NfcPresetData.WifiData -> {
                    logInfo("NFC", "Activating Wi-Fi preset", data.ssid)
                    WifiNetworkNdefData(
                        wifiName = data.ssid,
                        wifiProtection = if (data.isOpen) WifiNetworkNdefDataProtectionType.OPEN
                        else WifiNetworkNdefDataProtectionType.PASSWORD,
                        wifiPassword = if (data.isOpen) null else data.password
                    )
                }
                is NfcPresetData.LocationData -> {
                    logInfo("NFC", "Activating Location preset", "${data.latitude}, ${data.longitude}")
                    LocationNdefData(data.latitude, data.longitude)
                }
                is NfcPresetData.TelegramData -> {
                    val uri = if (data.usernameOrPhone.startsWith("+") || data.usernameOrPhone.startsWith("00")) {
                        "tg://msg?to=${data.usernameOrPhone}"
                    } else {
                        "https://t.me/${data.usernameOrPhone.removePrefix("@")}"
                    }
                    logInfo("NFC", "Activating Telegram preset", uri)
                    UriNdefData(uri)
                }
                is NfcPresetData.WhatsAppData -> {
                    val phone = data.phone.replace(Regex("[^0-9+]"), "")
                    val uri = "https://wa.me/$phone"
                    logInfo("NFC", "Activating WhatsApp preset", uri)
                    UriNdefData(uri)
                }
            }
            emulation.currentEmulatedNdefData = ndefData
            logSuccess("NFC", "Preset '${preset.title}' activated successfully")
            true
        } catch (e: Exception) {
            logError("NFC", "Failed to activate preset '${preset.title}'", e.message)
            false
        }
    }

    fun deactivate(context: Context) {
        try {
            NdefEmulation(context).currentEmulatedNdefData = null
            logInfo("NFC", "Emulation stopped")
        } catch (e: Exception) {
            logWarn("NFC", "Error stopping emulation", e.message)
        }
    }

    fun hasActiveEmulation(context: Context): Boolean {
        return try {
            NdefEmulation(context).currentEmulatedNdefData != null
        } catch (e: Exception) {
            false
        }
    }
}
