package com.example.libri.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.libri.R

@Composable
fun BookImage(
    url: String?,
    modifier: Modifier = Modifier,
    showShadow: Boolean = true,
    alignment: Alignment = Alignment.Center,
    shapePercent: Int = 10
) {
    val isPreview = LocalInspectionMode.current

    if (isPreview) {
        Image(
            painter = painterResource(R.drawable.demo_book_cover),
            contentDescription = "Book cover",
            modifier = modifier
                .clip(shape = RoundedCornerShape(percent = shapePercent))
                .apply {
                    if (showShadow) {
                        shadow(elevation = 10.dp)
                    }
                },
            contentScale = ContentScale.Crop,
            alignment = alignment
        )
    } else {
        AsyncImage(
            model = url,
            contentDescription = "Book cover",
            contentScale = ContentScale.Crop,
            alignment = alignment,
            modifier = modifier
                .clip(shape = RoundedCornerShape(percent = shapePercent))
                .apply {
                    if (showShadow) {
                        shadow(elevation = 10.dp)
                    }
                }
        )
//        GlideImage(
//            model = url,
//            contentDescription = "Book cover",
//            contentScale = ContentScale.Crop,
//            alignment = alignment,
//            loading = placeholder(R.drawable.placeholder),
//            failure = placeholder(R.drawable.placeholder),
//            transition = CrossFade,
//            modifier = modifier
//                .clip(shape = RoundedCornerShape(percent = 10))
//                .apply {
//                    if (showShadow) {
//                        shadow(elevation = 10.dp)
//                    }
//                }
//        ) {
//            it.thumbnail(
//                requestManager
//                    .asDrawable()
//                    .sizeMultiplier(0.1f)
//                    .load(url)
//            )
//        }
    }
}

@Preview
@Composable
private fun BookImagePreview() {
    BookImage(url = null)
}