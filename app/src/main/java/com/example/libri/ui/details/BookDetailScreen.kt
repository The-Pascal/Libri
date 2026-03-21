package com.example.libri.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.libri.domain.models.Book
import com.example.libri.ui.common.BookImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    viewModel: BookDetailViewModel,
    modifier: Modifier = Modifier
) {
    val book = remember { viewModel.book }
    MainContentScaffold(book = book)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MainContentScaffold(book: Book) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back button"
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        MainContent(
            book = book,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun MainContent(
    book: Book,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        TopBookContent(book = book)

        Column(
            modifier = Modifier
                .offset(y = (-2).dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface) // Must be opaque
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "About the Book",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "A few paragraphs of truncated text showing the description of the book here now. This is okay to be placed here, as this ia long text for howing the description of the book here now.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun TopBookContent(book: Book) {
    val surfaceColor = MaterialTheme.colorScheme.surface

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        BookImage(
            url = book.coverUrl,
            modifier = Modifier
                .fillMaxSize()
                .blur(40.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .alpha(0.8f)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            surfaceColor.copy(alpha = 0.2f),
                            surfaceColor.copy(alpha = 0.6f),
                            surfaceColor
                        )
                    )
                )
        ) {

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 28.dp)
        ) {
            BookImage(
                url = book.coverUrl,
                showShadow = true,
                modifier = Modifier
                    .size(width = 100.dp, height = 140.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = book.authors.joinToString(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = book.publishYear
                )
            }
        }
    }
}

@Preview
@Composable
private fun BookDetailPreview() {
    MainContentScaffold(Book(
        id = "abc123",
        title = "The Great Gatsby",
        authors = listOf("F. Scott Fitzgerald"),
        publishYear = "1925",
        coverUrl = "",
        isBookmarked = false
    ))
}