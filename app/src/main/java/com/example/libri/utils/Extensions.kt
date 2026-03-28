package com.example.libri.utils

import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer

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

fun Modifier.drawTextFade(): Modifier = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()

        val fadeBrush = Brush.verticalGradient(
            colors = listOf(Color.Black, Color.Transparent),
            startY = size.height * 0.5f,
            endY = size.height
        )

        drawRect(
            brush = fadeBrush,
            blendMode = BlendMode.DstIn
        )
    }