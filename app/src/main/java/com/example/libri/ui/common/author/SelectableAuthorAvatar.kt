package com.example.libri.ui.common.author

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.example.libri.R
import com.example.libri.ui.theme.LibriTheme
import com.example.libri.ui.theme.TerracottaFixed

@Composable
fun SelectableAuthorAvatar(
    model: Any?, // Can be URL String or Int resource
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
                .size(88.dp)
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
                AsyncImage(
                    model = model,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier,
                    colorFilter = colorFilter
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 14.sp
        )
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