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

    private val pureGson = Gson()

    override fun serialize(src: NfcPresetData, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.addProperty("type", src::class.simpleName)
        obj.add("data", pureGson.toJsonTree(src))
        return obj
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): NfcPresetData {
        val obj = json.asJsonObject
        val type = obj.get("type").asString
        val data = obj.get("data").asJsonObject

        return when (type) {
            "UrlData" -> pureGson.fromJson(data, NfcPresetData.UrlData::class.java)
            "UriData" -> pureGson.fromJson(data, NfcPresetData.UriData::class.java)
            "TextData" -> pureGson.fromJson(data, NfcPresetData.TextData::class.java)
            "ContactData" -> pureGson.fromJson(data, NfcPresetData.ContactData::class.java)
            "WifiData" -> pureGson.fromJson(data, NfcPresetData.WifiData::class.java)
            "LocationData" -> pureGson.fromJson(data, NfcPresetData.LocationData::class.java)
            "TelegramData" -> pureGson.fromJson(data, NfcPresetData.TelegramData::class.java)
            "WhatsAppData" -> pureGson.fromJson(data, NfcPresetData.WhatsAppData::class.java)
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
