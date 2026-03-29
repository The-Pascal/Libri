package com.example.libri.ui.common.author

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import com.example.libri.R
import com.example.libri.ui.theme.LibriTheme
import com.example.libri.ui.theme.TerracottaFixed
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImageContent
import kotlin.math.abs

@Composable
fun SelectableAuthorAvatar(
    model: Any?,
    name: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = true
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 4.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = CircleShape
                )
        ) {
            val imageModifier = Modifier
                .size(100.dp)
                .padding(4.dp)
                .clip(CircleShape)
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent,
                    shape = CircleShape
                )
                .padding(if (isSelected) 4.dp else 0.dp)
                .clip(CircleShape)

            val colorFilter = if (isSelected.not()) {
                ColorFilter.tint(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    blendMode = BlendMode.SrcOver
                )
            } else null

            val isPreview = LocalInspectionMode.current
            if (isPreview) {
                Image(
                    painter = painterResource(id = R.drawable.demo_book_cover),
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier,
                    colorFilter = colorFilter
                )
            } else {
                SubcomposeAsyncImage(
                    model = model,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier,
                    colorFilter = colorFilter,
                ) {
                    val state = painter.state.collectAsStateWithLifecycle()
                    when (state.value) {
                        is AsyncImagePainter.State.Loading -> {
                            AuthorInitialsFallback(
                                name = name,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        is AsyncImagePainter.State.Error, is AsyncImagePainter.State.Empty -> {
                            AuthorInitialsFallback(
                                name = name,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        is AsyncImagePainter.State.Success -> {
                            SubcomposeAsyncImageContent()
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 14.sp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun AuthorInitialsFallback(
    name: String,
    modifier: Modifier = Modifier
) {
    // Define these in your Type or Color.kt
    val authorColors = listOf(
        Color(0xFF2D545E), // Library Green (Deep & Academic)
        Color(0xFF7B3F4C), // Velvet Wine (Moody & Rich)
        Color(0xFF212931), // Deep Charcoal (Classic)
        Color(0xFFB25E46), // Terracotta Earth (Matches your theme)
        Color(0xFF6B4B2A), // Aged Leather (Warm Brown)
        Color(0xFF435B66), // Slate Blue (Professional)
        Color(0xFF161748)  // Midnight Ink (Deep Blue)
    )

    val backgroundColor = remember(name) {
        val index = abs(name.hashCode()) % authorColors.size
        authorColors[index]
    }

    val initials = name.split(" ")
        .mapNotNull { it.firstOrNull()?.toString() }
        .take(2)
        .joinToString("")
        .uppercase()

    Box(
        modifier = modifier.background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    }
}

@Preview
@Composable
private fun AuthorInitialsFallbackPreview() {
    LibriTheme {
        Surface() {
            AuthorInitialsFallback(name = "Elena Vance")
        }
    }
}

@Preview
@Composable
private fun AuthorAvatarPreview() {
    LibriTheme {
        Surface {
            SelectableAuthorAvatar(
                model = R.drawable.demo_book_cover,
                name = "JRR Rowling"
            )
        }
    }
}