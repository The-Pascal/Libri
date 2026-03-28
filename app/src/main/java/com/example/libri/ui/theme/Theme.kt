package com.example.libri.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EditorialColorScheme = lightColorScheme(
    primary = DeepCharcoal,
    onPrimary = SurfaceContainerLowest,
    secondary = DeepTerracotta,
    onSecondary = SurfaceContainerLowest,
    secondaryContainer = TerracottaContainerSecondary,
    tertiaryContainer = SageGreenContainer,

    surface = SurfaceBase,
    onSurface = DeepCharcoal,
    surfaceVariant = SurfaceVariantBlur,
    onSurfaceVariant = LightCharcoal.copy(alpha = 0.5f),

    // Tonal Layering Mapping
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHighest = SurfaceContainerHighest,

    outlineVariant = GhostOutline, // Ghost border suggestion

    inverseSurface = DarkBlue
)

@Composable
fun LibriTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EditorialColorScheme,
        typography = EditorialTypography,
        shapes = EditorialShapes,
        content = content
    )
}