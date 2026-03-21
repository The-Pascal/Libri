package com.example.libri.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.libri.R

@OptIn(ExperimentalTextApi::class)
val NotoSerifFamily = FontFamily(
    Font(
        resId = R.font.noto_serif_variable,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400) // Default weight
        )
    ),
    Font(
        resId = R.font.noto_serif_italic_variable,
        style = FontStyle.Italic,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400)
        )
    )
)

@OptIn(ExperimentalTextApi::class)
val PlusJakartaSansFamily = FontFamily(
    Font(
        resId = R.font.plus_jakarta_sans_variable, // Ensure this file is in res/font
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400)
        )
    )
)

val EditorialTypography = Typography(
    // Editorial Voice (Noto Serif)
    displayLarge = TextStyle(
        fontFamily = NotoSerifFamily,
        fontWeight = FontWeight(600),
        fontSize = 36.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = NotoSerifFamily,
        fontWeight = FontWeight(600),
        fontSize = 28.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = NotoSerifFamily,
        fontWeight = FontWeight(400),
        fontSize = 14.sp
    ),

    // Functional Voice (Plus Jakarta Sans)
    titleMedium = TextStyle(
        fontFamily = NotoSerifFamily,
        fontWeight = FontWeight(500),
        fontSize = 18.sp,
        color = DeepCharcoal
    ),
    bodyMedium = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight(400),
        fontSize = 14.sp,
        color = DeepCharcoal
    ),
    labelMedium = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight(600), // SemiBold
        fontSize = 12.sp,
        color = LightCharcoal
    ),
    labelSmall = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight(400), // SemiBold
        fontSize = 10.sp
    ),
)