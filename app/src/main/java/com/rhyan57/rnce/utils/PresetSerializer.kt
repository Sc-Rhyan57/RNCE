package com.rhyan57.rnce.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import com.rhyan57.rnce.model.NfcPreset
import com.rhyan57.rnce.model.NfcPresetData
import java.lang.reflect.Type

class NfcPresetDataAdapter : JsonSerializer<NfcPresetData>, JsonDeserializer<NfcPresetData> {
    override fun serialize(src: NfcPresetData, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.addProperty("type", src::class.simpleName)
        obj.add("data", context.serialize(src))
        return obj
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): NfcPresetData {
        val obj = json.asJsonObject
        val type = obj.get("type").asString
        val data = obj.get("data").asJsonObject
        return when (type) {
            "UrlData" -> context.deserialize(data, NfcPresetData.UrlData::class.java)
            "UriData" -> context.deserialize(data, NfcPresetData.UriData::class.java)
            "TextData" -> context.deserialize(data, NfcPresetData.TextData::class.java)
            "ContactData" -> context.deserialize(data, NfcPresetData.ContactData::class.java)
            "WifiData" -> context.deserialize(data, NfcPresetData.WifiData::class.java)
            "LocationData" -> context.deserialize(data, NfcPresetData.LocationData::class.java)
            "TelegramData" -> context.deserialize(data, NfcPresetData.TelegramData::class.java)
            "WhatsAppData" -> context.deserialize(data, NfcPresetData.WhatsAppData::class.java)
            else -> NfcPresetData.TextData("")
        }
    }
}

object PresetSerializer {
    private val gson: Gson = GsonBuilder()
        .registerTypeHierarchyAdapter(NfcPresetData::class.java, NfcPresetDataAdapter())
        .create()

    fun toJson(presets: List<NfcPreset>): String = gson.toJson(presets)

    fun fromJson(json: String): List<NfcPreset> {
        return try {
            val type = object : TypeToken<List<NfcPreset>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
