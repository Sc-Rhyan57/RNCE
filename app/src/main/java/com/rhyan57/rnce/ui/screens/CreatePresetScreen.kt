package com.rhyan57.rnce.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rhyan57.rnce.model.*
import com.rhyan57.rnce.utils.IconName
import com.rhyan57.rnce.utils.NfcTypeIconName
import com.rhyan57.rnce.utils.toImageVector
import java.util.UUID

@Composable
fun CreatePresetScreen(
    editPreset: NfcPreset? = null,
    onSave: (NfcPreset) -> Unit,
    onCancel: () -> Unit
) {
    var title         by remember { mutableStateOf(editPreset?.title ?: "") }
    var description   by remember { mutableStateOf(editPreset?.description ?: "") }
    var selectedType  by remember { mutableStateOf(editPreset?.nfcType ?: NfcType.URL) }
    var selectedColor by remember { mutableLongStateOf(editPreset?.colorHex ?: PresetColor.PRESETS[0].hex) }
    var selectedIcon  by remember { mutableStateOf(editPreset?.iconName ?: NfcTypeIconName(NfcType.URL)) }

    val d = editPreset?.nfcData
    var urlValue      by remember { mutableStateOf((d as? NfcPresetData.UrlData)?.url ?: "") }
    var uriValue      by remember { mutableStateOf((d as? NfcPresetData.UriData)?.uri ?: "") }
    var textValue     by remember { mutableStateOf((d as? NfcPresetData.TextData)?.text ?: "") }
    var cFirst        by remember { mutableStateOf((d as? NfcPresetData.ContactData)?.firstName ?: "") }
    var cLast         by remember { mutableStateOf((d as? NfcPresetData.ContactData)?.lastName ?: "") }
    var cPhone        by remember { mutableStateOf((d as? NfcPresetData.ContactData)?.phone ?: "") }
    var cEmail        by remember { mutableStateOf((d as? NfcPresetData.ContactData)?.email ?: "") }
    var cCompany      by remember { mutableStateOf((d as? NfcPresetData.ContactData)?.company ?: "") }
    var cTitle        by remember { mutableStateOf((d as? NfcPresetData.ContactData)?.title ?: "") }
    var cWebsite      by remember { mutableStateOf((d as? NfcPresetData.ContactData)?.website ?: "") }
    var cNotes        by remember { mutableStateOf((d as? NfcPresetData.ContactData)?.notes ?: "") }
    var wSsid         by remember { mutableStateOf((d as? NfcPresetData.WifiData)?.ssid ?: "") }
    var wPass         by remember { mutableStateOf((d as? NfcPresetData.WifiData)?.password ?: "") }
    var wOpen         by remember { mutableStateOf((d as? NfcPresetData.WifiData)?.isOpen ?: false) }
    var gLat          by remember { mutableStateOf((d as? NfcPresetData.LocationData)?.latitude?.toString() ?: "") }
    var gLon          by remember { mutableStateOf((d as? NfcPresetData.LocationData)?.longitude?.toString() ?: "") }
    var tgTarget      by remember { mutableStateOf((d as? NfcPresetData.TelegramData)?.usernameOrPhone ?: "") }
    var waPhone       by remember { mutableStateOf((d as? NfcPresetData.WhatsAppData)?.phone ?: "") }

    val pc = Color(selectedColor)
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = pc,
        focusedLabelColor = pc,
        cursorColor = pc,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 16.dp, top = 48.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onCancel) {
                    Icon(Icons.Outlined.ArrowBack, null, tint = MaterialTheme.colorScheme.onBackground)
                }
                Text(
                    if (editPreset != null) "Edit Preset" else "New Preset",
                    fontSize = 24.sp, fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        item {
            FormSection("NFC Type")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(NfcType.values()) { type ->
                    FilterChip(
                        selected = type == selectedType,
                        onClick = { selectedType = type; selectedIcon = NfcTypeIconName(type) },
                        label = { Text(type.label, fontSize = 12.sp, fontWeight = FontWeight.Medium) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = pc.copy(0.2f),
                            selectedLabelColor = pc,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = type == selectedType,
                            selectedBorderColor = pc.copy(0.5f),
                            unselectedBorderColor = MaterialTheme.colorScheme.outline.copy(0.2f)
                        )
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            FormSection("Color")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(PresetColor.PRESETS) { preset ->
                    val c = Color(preset.hex)
                    val sel = preset.hex == selectedColor
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(c)
                            .then(if (sel) Modifier.border(3.dp, MaterialTheme.colorScheme.onBackground, CircleShape) else Modifier)
                            .clickable { selectedColor = preset.hex },
                        contentAlignment = Alignment.Center
                    ) {
                        if (sel) Icon(Icons.Outlined.Check, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            FormSection("Icon")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(IconName.values()) { icon ->
                    val sel = icon == selectedIcon
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (sel) pc.copy(0.2f) else MaterialTheme.colorScheme.surface)
                            .then(if (sel) Modifier.border(1.5.dp, pc, RoundedCornerShape(12.dp)) else Modifier.border(1.dp, MaterialTheme.colorScheme.outline.copy(0.1f), RoundedCornerShape(12.dp)))
                            .clickable { selectedIcon = icon },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon.toImageVector(), null, tint = if (sel) pc else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(22.dp))
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            FormSection("Details")
            Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title, onValueChange = { title = it },
                    label = { Text("Title *") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors, singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = description, onValueChange = { description = it },
                    label = { Text("Description (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors, maxLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            FormSection("NFC Data — ${selectedType.label}")
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                when (selectedType) {
                    NfcType.URL      -> SingleField("URL (https://...)", urlValue, { urlValue = it }, fieldColors, KeyboardType.Uri)
                    NfcType.URI      -> SingleField("URI", uriValue, { uriValue = it }, fieldColors)
                    NfcType.TEXT     -> SingleField("Plain Text", textValue, { textValue = it }, fieldColors, maxLines = 6)
                    NfcType.TELEGRAM -> SingleField("@username or +55...", tgTarget, { tgTarget = it }, fieldColors)
                    NfcType.WHATSAPP -> SingleField("Phone (+55 11 99999-9999)", waPhone, { waPhone = it }, fieldColors, KeyboardType.Phone)
                    NfcType.CONTACT  -> ContactFields(cFirst,{cFirst=it},cLast,{cLast=it},cPhone,{cPhone=it},cEmail,{cEmail=it},cCompany,{cCompany=it},cTitle,{cTitle=it},cWebsite,{cWebsite=it},cNotes,{cNotes=it},fieldColors)
                    NfcType.WIFI     -> WifiFields(wSsid,{wSsid=it},wPass,{wPass=it},wOpen,{wOpen=it},fieldColors)
                    NfcType.LOCATION -> GeoFields(gLat,{gLat=it},gLon,{gLon=it},fieldColors)
                }
            }
            Spacer(Modifier.height(28.dp))
        }

        item {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel, modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(0.2f))
                ) { Text("Cancel", fontWeight = FontWeight.Bold) }

                Button(
                    onClick = {
                        if (title.isBlank()) return@Button
                        val nfcData: NfcPresetData = when (selectedType) {
                            NfcType.URL      -> NfcPresetData.UrlData(urlValue)
                            NfcType.URI      -> NfcPresetData.UriData(uriValue)
                            NfcType.TEXT     -> NfcPresetData.TextData(textValue)
                            NfcType.CONTACT  -> NfcPresetData.ContactData(cFirst,cLast,cPhone,cEmail,cCompany,cTitle,cWebsite,cNotes)
                            NfcType.WIFI     -> NfcPresetData.WifiData(wSsid,wPass,wOpen)
                            NfcType.LOCATION -> NfcPresetData.LocationData(gLat.toDoubleOrNull() ?: 0.0, gLon.toDoubleOrNull() ?: 0.0)
                            NfcType.TELEGRAM -> NfcPresetData.TelegramData(tgTarget)
                            NfcType.WHATSAPP -> NfcPresetData.WhatsAppData(waPhone)
                        }
                        onSave(NfcPreset(
                            id = editPreset?.id ?: UUID.randomUUID().toString(),
                            title = title.trim(),
                            description = description.trim(),
                            nfcType = selectedType,
                            iconName = selectedIcon,
                            colorHex = selectedColor,
                            createdAt = editPreset?.createdAt ?: System.currentTimeMillis(),
                            nfcData = nfcData
                        ))
                    },
                    modifier = Modifier.weight(1f).height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = pc, contentColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    enabled = title.isNotBlank()
                ) {
                    Icon(Icons.Outlined.Save, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(if (editPreset != null) "Update" else "Create", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun FormSection(text: String) {
    Text(
        text.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 1.2.sp,
        modifier = Modifier.padding(start = 16.dp, bottom = 10.dp)
    )
}

@Composable
private fun SingleField(
    label: String, value: String, onChange: (String) -> Unit,
    colors: TextFieldColors,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value, onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        colors = colors, singleLine = maxLines == 1, maxLines = maxLines,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
private fun ContactFields(
    first: String, onFirst: (String) -> Unit,
    last: String, onLast: (String) -> Unit,
    phone: String, onPhone: (String) -> Unit,
    email: String, onEmail: (String) -> Unit,
    company: String, onCompany: (String) -> Unit,
    jobTitle: String, onJobTitle: (String) -> Unit,
    website: String, onWebsite: (String) -> Unit,
    notes: String, onNotes: (String) -> Unit,
    colors: TextFieldColors
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(first, onFirst, label = { Text("First Name *") }, modifier = Modifier.weight(1f), colors = colors, singleLine = true, shape = RoundedCornerShape(12.dp))
            OutlinedTextField(last,  onLast,  label = { Text("Last Name") },    modifier = Modifier.weight(1f), colors = colors, singleLine = true, shape = RoundedCornerShape(12.dp))
        }
        OutlinedTextField(phone,   onPhone,   label = { Text("Phone") },   modifier = Modifier.fillMaxWidth(), colors = colors, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), shape = RoundedCornerShape(12.dp))
        OutlinedTextField(email,   onEmail,   label = { Text("Email") },   modifier = Modifier.fillMaxWidth(), colors = colors, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), shape = RoundedCornerShape(12.dp))
        OutlinedTextField(company, onCompany, label = { Text("Company") }, modifier = Modifier.fillMaxWidth(), colors = colors, singleLine = true, shape = RoundedCornerShape(12.dp))
        OutlinedTextField(jobTitle,onJobTitle,label = { Text("Job Title") },modifier = Modifier.fillMaxWidth(), colors = colors, singleLine = true, shape = RoundedCornerShape(12.dp))
        OutlinedTextField(website, onWebsite, label = { Text("Website") }, modifier = Modifier.fillMaxWidth(), colors = colors, singleLine = true, shape = RoundedCornerShape(12.dp))
        OutlinedTextField(notes,   onNotes,   label = { Text("Notes") },   modifier = Modifier.fillMaxWidth(), colors = colors, maxLines = 3, shape = RoundedCornerShape(12.dp))
    }
}

@Composable
private fun WifiFields(
    ssid: String, onSsid: (String) -> Unit,
    password: String, onPassword: (String) -> Unit,
    isOpen: Boolean, onOpen: (Boolean) -> Unit,
    colors: TextFieldColors
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(ssid, onSsid, label = { Text("Network Name (SSID)") }, modifier = Modifier.fillMaxWidth(), colors = colors, singleLine = true, shape = RoundedCornerShape(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)).padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Open network (no password)", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f), fontSize = 14.sp)
            Switch(checked = isOpen, onCheckedChange = onOpen,
                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.onPrimary, checkedTrackColor = MaterialTheme.colorScheme.primary, uncheckedThumbColor = MaterialTheme.colorScheme.outline, uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant))
        }
        if (!isOpen) {
            OutlinedTextField(password, onPassword, label = { Text("Password (min 8 chars)") }, modifier = Modifier.fillMaxWidth(), colors = colors, singleLine = true, shape = RoundedCornerShape(12.dp))
        }
    }
}

@Composable
private fun GeoFields(
    lat: String, onLat: (String) -> Unit,
    lon: String, onLon: (String) -> Unit,
    colors: TextFieldColors
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(lat, onLat, label = { Text("Latitude (−90 to 90)") }, modifier = Modifier.fillMaxWidth(), colors = colors, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), shape = RoundedCornerShape(12.dp))
        OutlinedTextField(lon, onLon, label = { Text("Longitude (−180 to 180)") }, modifier = Modifier.fillMaxWidth(), colors = colors, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), shape = RoundedCornerShape(12.dp))
    }
}
