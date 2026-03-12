package com.example.libri.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.libri.R

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun BookImage(
    url: String?,
    modifier: Modifier = Modifier,
    showShadow: Boolean = true
) {
    val context = LocalContext.current
    val requestManager = remember(context) { Glide.with(context) }

    GlideImage(
        model = url,
        contentDescription = "Book cover",
        contentScale = ContentScale.Crop,
        loading = placeholder(R.drawable.placeholder),
        failure = placeholder(R.drawable.placeholder),
        transition = CrossFade,
        modifier = modifier
            .clip(shape = RoundedCornerShape(percent = 10))
            .apply {
                if (showShadow) {
                    shadow(elevation = 10.dp)
                }
            }
    ) {
        it.thumbnail(
            requestManager
                .asDrawable()
                .sizeMultiplier(0.1f)
                .load(url)
        )
    }
}