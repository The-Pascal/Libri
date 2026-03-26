package com.example.libri.ui.common

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.libri.R
import com.example.libri.ui.theme.LibriTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AuthorAvatar(
    model: Any?, // Can be URL String or Int resource
    name: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(72.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .border(2.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
        ) {
            if (model is Int) {
                Image(
                    painter = painterResource(id = model),
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(72.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                )
            } else {
                GlideImage(
                    model = model,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(72.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
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
            AuthorAvatar(
                model = R.drawable.demo_book_cover,
                name = "JRR Rowling test"
            )
        }
    }
}