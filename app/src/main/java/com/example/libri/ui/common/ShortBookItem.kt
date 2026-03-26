package com.example.libri.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.libri.domain.models.Book
import com.example.libri.ui.theme.LibriTheme
import com.example.libri.utils.toSentenceCase
import java.util.Locale

@Composable
fun ShortBookItem(
    book: Book,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(150.dp)
            .padding(bottom = 8.dp)
    ) {
        BookImage(
            url = book.imageLinks?.medium,
            showShadow = false,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f/3f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = book.title.toSentenceCase(),
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = book.authors.joinToString(),
            style = MaterialTheme.typography.labelMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
        isBookmarked = true
    )
    LibriTheme {
        ShortBookItem(book)
    }
}