package com.example.libri.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.libri.R
import com.example.libri.domain.models.Book
import com.example.libri.ui.theme.LibriTheme
import com.example.libri.ui.theme.YellowColor
import com.example.libri.utils.toReadableCount
import com.example.libri.utils.toSentenceCase

@Composable
fun DarkShortBookItem(
    book: Book,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(150.dp)
            .padding(bottom = 8.dp)
    ) {
        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            BookImage(
                url = book.imageLinks?.medium,
                showShadow = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f/3f)
            )
            book.downloadCount?.let {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(top = 8.dp, end = 8.dp)
                        .clip(CircleShape)
                        .background(color = YellowColor)
                        .padding(4.dp)
                        .size(36.dp)
                ) {
                    Text(
                        text = book.downloadCount.toReadableCount(),
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "Reads",
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 8.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = book.title,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = book.authors.joinToString().uppercase(),
            style = MaterialTheme.typography.labelMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp),
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
        )
    }
}

@Preview(showBackground = true, backgroundColor = android.graphics.Color.WHITE.toLong())
@Composable
private fun ShortBookItemPreview() {
    val book = Book(
        id = "abc123",
        title = "Harry Potter Gauntlet",
        authors = listOf("J.K. Rowling"),
        coverUrl = "https://www.image.com",
        publishYear = "1997",
        isBookmarked = true,
        averageRating = 4.5,
        downloadCount = 15_000
    )
    LibriTheme {
        Surface {
            DarkShortBookItem(
                book,
                modifier = Modifier.background(color = MaterialTheme.colorScheme.inverseSurface)
            )
        }
    }
}