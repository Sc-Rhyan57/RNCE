package com.rhyan57.rnce.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.rhyan57.rnce.model.NfcType

enum class IconName(val label: String) {
    Nfc("NFC"), Link("Link"), Language("Web"), Person("Contact"),
    Wifi("Wi-Fi"), LocationOn("Location"), TextFields("Text"),
    Send("Telegram"), Chat("WhatsApp"), QrCode("QR Code"),
    Share("Share"), Star("Star"), Favorite("Favorite"),
    Bolt("Bolt"), Fingerprint("Fingerprint"), Shield("Shield"),
    Lock("Lock"), Key("Key"), Smartphone("Phone"), Laptop("Device"),
    Cloud("Cloud"), Code("Code"), SettingsIcon("Settings"),
    Work("Work"), School("School"), Home("Home"), Store("Store"),
    Email("Email"), Phone("Phone"), Badge("Badge")
}

fun IconName.toImageVector(): ImageVector = when (this) {
    IconName.Nfc         -> Icons.Outlined.Nfc
    IconName.Link        -> Icons.Outlined.Link
    IconName.Language    -> Icons.Outlined.Language
    IconName.Person      -> Icons.Outlined.Person
    IconName.Wifi        -> Icons.Outlined.Wifi
    IconName.LocationOn  -> Icons.Outlined.LocationOn
    IconName.TextFields  -> Icons.Outlined.TextFields
    IconName.Send        -> Icons.Outlined.Send
    IconName.Chat        -> Icons.Outlined.Chat
    IconName.QrCode      -> Icons.Outlined.QrCode
    IconName.Share       -> Icons.Outlined.Share
    IconName.Star        -> Icons.Outlined.Star
    IconName.Favorite    -> Icons.Outlined.Favorite
    IconName.Bolt        -> Icons.Outlined.Bolt
    IconName.Fingerprint -> Icons.Outlined.Fingerprint
    IconName.Shield      -> Icons.Outlined.Shield
    IconName.Lock        -> Icons.Outlined.Lock
    IconName.Key         -> Icons.Outlined.Key
    IconName.Smartphone  -> Icons.Outlined.Smartphone
    IconName.Laptop      -> Icons.Outlined.Laptop
    IconName.Cloud       -> Icons.Outlined.Cloud
    IconName.Code        -> Icons.Outlined.Code
    IconName.SettingsIcon-> Icons.Outlined.Settings
    IconName.Work        -> Icons.Outlined.Work
    IconName.School      -> Icons.Outlined.School
    IconName.Home        -> Icons.Outlined.Home
    IconName.Store       -> Icons.Outlined.Store
    IconName.Email       -> Icons.Outlined.Email
    IconName.Phone       -> Icons.Outlined.Phone
    IconName.Badge       -> Icons.Outlined.Badge
}

fun NfcTypeIconName(type: NfcType): IconName = when (type) {
    NfcType.URL       -> IconName.Link
    NfcType.URI       -> IconName.Language
    NfcType.TEXT      -> IconName.TextFields
    NfcType.CONTACT   -> IconName.Person
    NfcType.WIFI      -> IconName.Wifi
    NfcType.LOCATION  -> IconName.LocationOn
    NfcType.TELEGRAM  -> IconName.Send
    NfcType.WHATSAPP  -> IconName.Chat
}
