package com.example.libri.data.mapper.extensions

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

fun JsonElement?.toDisplayString(): String {
    if (this == null) return "No description available."
    return try {
        // Case 1: Raw String
        if (this is JsonPrimitive) return this.content
        // Case 2: Object { "value": "..." }
        if (this is JsonObject) return this["value"]?.jsonPrimitive?.content ?: ""
        ""
    } catch (e: Exception) {
        "A literary gem currently being curated."
    }
}