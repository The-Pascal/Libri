package com.example.libri.data.mapper

import com.google.gson.JsonElement
import com.google.gson.JsonObject

fun JsonElement?.toBioText(): String {
    if (this == null || isJsonNull) return ""
    if (isJsonPrimitive) return asString
    if (isJsonObject) {
        val obj = this as JsonObject
        val value = obj.get("value") ?: return ""
        if (value.isJsonPrimitive) return value.asString
    }
    return ""
}
