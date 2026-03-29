package com.example.libri.ui.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.libri.R
import com.example.libri.domain.models.Book
import com.example.libri.domain.models.UiState
import com.example.libri.ui.common.author.AuthorAvatar
import com.example.libri.ui.common.BookImage
import com.example.libri.ui.common.DarkShortBookItem
import com.example.libri.ui.common.LibriTopAppBar
import com.example.libri.ui.common.SectionHeader
import com.example.libri.ui.common.ShortBookItem
import com.example.libri.ui.common.ShortBookItemShimmer
import com.example.libri.ui.theme.DarkGreenContainer
import com.example.libri.ui.theme.LibriTheme
import com.example.libri.ui.theme.NotoSerifFamily
import com.example.libri.ui.theme.YellowColor
import com.example.libri.utils.BookAuthor
import com.example.libri.utils.CategoryGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToSearchScreen: () -> Unit,
    navigateToBookDetails: (Book) -> Unit,
    modifier: Modifier,
) {
    val freeTreasureBooks by viewModel.freeTreasureBooks.collectAsStateWithLifecycle()
    val booksByGenre by viewModel.booksByGenre.collectAsStateWithLifecycle()
    val selectedGenre by viewModel.selectedCategory.collectAsStateWithLifecycle()

    Log.d("TestGenre", "Books by genre - $booksByGenre")

    MainContent(
        freeTreasureBooks = freeTreasureBooks,
        navigateToBookDetails = navigateToBookDetails,
        booksByGenre = booksByGenre,
        selectedGenre = selectedGenre,
        onGenreSelected = { viewModel.onGenreSelected(it) },
        modifier = modifier
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MainContent(
    freeTreasureBooks: UiState,
    navigateToBookDetails: (Book) -> Unit,
    booksByGenre: UiState,
    selectedGenre: CategoryGroup,
    onGenreSelected: (CategoryGroup) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { LibriTopAppBar() },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 72.dp)
        ) {
            item(key = "editor_choice") {
                EditorChoiceSection(
                    book = Book(
                        id = "123",
                        title = "The Alchemist's Journal",
                        authors = listOf(""),
                        coverUrl = "https://covers.openlibrary.org/b/id/15121528-L.jpg",
                        publishYear = "1997",
                        isBookmarked = true
                    )
                )
            }

            item(key = "trending_by_genre") {
                TrendingByGenreSection(
                    state = booksByGenre,
                    selectedGenre = selectedGenre,
                    onGenreSelected = onGenreSelected,
                    navigateToBookDetails = navigateToBookDetails
                )
            }

            item(key = "popular_authors") {
                PopularAuthorsSection()
            }

            item(key = "award_winner_section") {
                TreasureBooksSection(
                    uiState = freeTreasureBooks,
                    navigateToBookDetails = navigateToBookDetails
                )
            }

            item(key = "bottom_quote") {
                BottomQuote()
            }
        }
    }
}

@Preview
@Composable
private fun EditorChoicePreview() {
    LibriTheme {
        Surface() {
            EditorChoiceSection(
                book = Book(
                    id = "123",
                    title = "The Alchemist's Journal",
                    authors = listOf(""),
                    coverUrl = "https://covers.openlibrary.org/b/id/15121528-L.jpg",
                    publishYear = "1997",
                    isBookmarked = true
                )
            )
        }
    }
}

@Composable
fun EditorChoiceSection(
    book: Book,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var imageWidthPx by remember { mutableIntStateOf(0) }

    val imageWidthDp = remember(imageWidthPx) {
        with(density) {
            imageWidthPx.toDp()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(440.dp)
            .padding(16.dp)
            .clip(MaterialTheme.shapes.large)
            .background(DarkGreenContainer),
        contentAlignment = Alignment.Center
    ) {
        ShadowedBookImage(
            modifier = Modifier.offset(y = (-10).dp),
            url = book.coverUrl
        ) {
            imageWidthPx = it.width
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(12.dp)
                .clip(MaterialTheme.shapes.large)
        ) {
            Box(
                modifier = Modifier.matchParentSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                if (imageWidthDp > 0.dp) {
                    BookImage(
                        url = book.coverUrl,
                        modifier = Modifier
                            .width(imageWidthDp)
                            .aspectRatio(2f / 3f)
                            .offset(y = (-80).dp)
                            .align(Alignment.BottomCenter)
                            .blur(50.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                            .alpha(0.9f),
                    )
                }
            }

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.2f), //: Ghost Border
                        shape = MaterialTheme.shapes.large
                    )
            )

            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "EDITOR'S CHOICE",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    letterSpacing = TextUnit(1f, TextUnitType.Sp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.surfaceContainerLowest
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .height(IntrinsicSize.Min),
                ) {
                    Button(
                        onClick = {},
                        contentPadding = PaddingValues(horizontal = 28.dp),
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            text = "Start Reading",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }

                    Surface(
                        onClick = {},
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        contentColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.2f), //: Ghost Border
                        ),
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                    ) {
                        Icon(
                            painter = if(!book.isBookmarked) {
                                painterResource(R.drawable.favorite_outline_icon)
                            } else {
                                painterResource(R.drawable.baseline_favorite_24)
                            },
                            contentDescription = "favorite icon",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun ShadowedBookImage(
    modifier: Modifier = Modifier,
    url: String? = null,
    onSizeChanged: (IntSize) -> Unit
) {
    Box(modifier) {
        Box(
            modifier = Modifier
                .padding(vertical = 48.dp)
                .aspectRatio(2f / 3f)
                .offset(x = 10.dp, y = 10.dp) // Intentional Asymmetry
                .blur(12.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(MaterialTheme.colorScheme.primary)
            )
        }

        BookImage(
            url = url,
            modifier = Modifier
                .padding(vertical = 48.dp)
                .aspectRatio(2f / 3f)
                .onSizeChanged { onSizeChanged(it) }
        )
    }
}

@Composable
private fun TreasureBooksSection(
    uiState: UiState,
    navigateToBookDetails: (Book) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.inverseSurface)
            .padding(vertical = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_diamond_24),
                contentDescription = "Free Treasure icon",
                tint = YellowColor
            )
            SectionHeader(
                text = "Free Treasures",
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (uiState) {
            is UiState.Error -> Text("Error")

            is UiState.Loading -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) {
                    items(5) {
                        ShortBookItemShimmer()
                    }
                }
            }

            is UiState.Success -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) {
                    items(items = uiState.books) {
                        DarkShortBookItem(
                            book = it,
                            modifier = Modifier.clickable(enabled = true) {
                                navigateToBookDetails(it)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TrendingByGenreSection(
    state: UiState,
    selectedGenre: CategoryGroup,
    onGenreSelected: (CategoryGroup) -> Unit,
    navigateToBookDetails: (Book) -> Unit
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionHeader(
                text = "Trending Now",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            )
            Text(
                text = "View All",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(end = 16.dp)
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                horizontal = 16.dp, vertical = 12.dp
            ),
        ) {
            items(
                items = CategoryGroup.entries,
                key = { it.displayName }
            ) { genre ->
                val isSelected = genre == selectedGenre
                FilterChip(
                    label = {
                        Text(
                            text = genre.displayName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = if (isSelected) { FontWeight(800) } else { FontWeight(400) },
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(8.dp)
                        )
                    },
                    selected = isSelected,
                    onClick = { onGenreSelected(genre) },
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)
                    )
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        ShortItemsList(state, navigateToBookDetails)
    }
}

@Composable
private fun PopularAuthorsSection(
    modifier: Modifier = Modifier,
    isPreview: Boolean = false
) {
    Column(modifier) {
        SectionHeader(
            text = "Popular Authors",
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = BookAuthor.all) {
                AuthorAvatar(
                    model = if (isPreview) R.drawable.demo_book_cover else it.imageUrl,
                    name = it.displayName
                )
            }
        }
    }
}

@Composable
private fun ShortItemsList(
    state: UiState,
    navigateToBookDetails: (Book) -> Unit,
) {
    when (state) {
        is UiState.Error -> Text("Error")

        is UiState.Loading -> {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                items(5) {
                    ShortBookItemShimmer()
                }
            }
        }

        is UiState.Success -> {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                items(items = state.books) {
                    ShortBookItem(
                        book = it,
                        modifier = Modifier.clickable(enabled = true) {
                            navigateToBookDetails(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomQuote(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 36.dp, vertical = 24.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.quote_filled_icon),
            contentDescription = "Quote icon",
            tint = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = "\"A book is a heart that only beats when another heart is near.\"",
            fontFamily = NotoSerifFamily,
            fontStyle = FontStyle.Italic,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
        Text(
            text = "-- JULIEN GREEN",
            style = MaterialTheme.typography.headlineSmall,
            letterSpacing = TextUnit(1.5f, TextUnitType.Sp),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 12.sp,
            fontWeight = FontWeight(800)
        )
    }
}
//
//@Preview
//@Composable
//private fun BottomQuotePreview() {
//    LibriTheme {
//        Surface {
//            BottomQuote()
//        }
//    }
//}

//@Preview(showSystemUi = true)
//@Composable
//private fun MainContentPreview() {
//    val book = Book(
//        id = "abc123",
//        title = "Harry Potter Gauntlet",
//        authors = listOf("J.K. Rowling"),
//        coverUrl = "https://www.image.com",
//        publishYear = "1997",
//        isBookmarked = true
//    )
//    val uiState = UiState.Success(listOf(book, book, book))
//
//    LibriTheme {
//        MainContent(
//            trendingState = uiState,
//            navigateToBookDetails = {},
//            booksByGenre = uiState,
//            selectedGenre = BookGenre.FANTASY,
//            onGenreSelected = {}
//        )
//    }
//}

//@Preview
//@Composable
//private fun PopularAuthorsPreview() {
//    LibriTheme {
//        Surface {
//            PopularAuthorsSection(isPreview = true)
//        }
//    }
//}
//
//@Preview
//@Composable
//private fun TestPreview() {
//    val book = Book(
//        id = "abc123",
//        title = "Harry Potter Gauntlet",
//        authors = listOf("J.K. Rowling"),
//        coverUrl = "https://www.image.com",
//        publishYear = "1997",
//        isBookmarked = true
//    )
//
//    LibriTheme {
//        Surface {
//            TrendingByGenreSection(
//                state = UiState.Success(listOf(book, book, book)),
//                selectedGenre = CategoryGroup.FICTION,
//                onGenreSelected = {},
//                navigateToBookDetails = {}
//            )
//        }
//    }
//}

@Preview
@Composable
private fun TrendingNowSectionPreview() {
    val book = Book(
        id = "abc123",
        title = "Harry Potter Gauntlet",
        authors = listOf("J.K. Rowling"),
        coverUrl = "https://www.image.com",
        publishYear = "1997",
        isBookmarked = true
    )

    LibriTheme {
        Surface() {
            TreasureBooksSection(
                uiState = UiState.Success(listOf(book, book, book)),
                navigateToBookDetails = { },
            )
        }
    }
}
