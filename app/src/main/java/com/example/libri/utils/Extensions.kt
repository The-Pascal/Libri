package com.example.libri.utils

fun String.removeIdPrefix(): String = this.removePrefix("/works/")

fun String.toSentenceCase(): String {
    if (this.isBlank()) return this
    return this.lowercase().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}

fun Int.toReadableCount(): String {
    return when {
        this >= 1_000_000 -> "${String.format("%.1f", this / 1_000_000.0)}M"
        this >= 1_000 -> "${String.format("%.1f", this / 1_000.0)}k"
        else -> this.toString()
    }
}