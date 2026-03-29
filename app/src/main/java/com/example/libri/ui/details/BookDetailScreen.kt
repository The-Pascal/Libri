package com.example.libri.ui.details

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.libri.R
import com.example.libri.domain.models.Authors
import com.example.libri.domain.models.Book
import com.example.libri.domain.models.BookDetails
import com.example.libri.domain.models.BookStats
import com.example.libri.ui.common.BookImage
import com.example.libri.ui.common.HtmlDescription
import com.example.libri.ui.common.author.SelectableAuthorAvatar
import com.example.libri.ui.theme.LibriTheme
import com.example.libri.ui.theme.LightCharcoal
import com.example.libri.utils.ApiType
import com.example.libri.utils.drawTextFade
import com.example.libri.utils.thenIf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    viewModel: BookDetailViewModel,
    modifier: Modifier = Modifier
) {
    val basicBookDetails by viewModel.bookArgs.collectAsStateWithLifecycle()
    val secondaryBookDetails by viewModel.bookDetails.collectAsStateWithLifecycle()

    MainContentScaffold(
        modifier = Modifier,
        basicBookDetails = basicBookDetails,
        secondaryBookDetails = secondaryBookDetails
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MainContentScaffold(
    secondaryBookDetails: BookDetailViewModel.BookDetailsUIModel,
    basicBookDetails: BookDetailViewModel.Args,
    modifier: Modifier = Modifier
) {
    val isStartedReading = false
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
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
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(start = 16.dp, end = 16.dp)
                        .shadow(25.dp)
                ) {
                    ActionButtonBottomCenter(
                        isStartedReading = isStartedReading,
                        onClick = {},
                    )
                }
        }
    ) { innerPadding ->
        MainContent(
            basicBookDetails = basicBookDetails,
            secondaryBookDetails = secondaryBookDetails,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun ActionButtonBottomCenter(
    isStartedReading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val background = if (isStartedReading) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.primary
    }

    val icon = if (isStartedReading) {
        R.drawable.filled_edit_icon
    } else {
        R.drawable.outline_book_ribbon_24
    }

    val text = if (isStartedReading) {
        "Update Progress"
    } else {
        "Start Reading"
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(
                background,
                shape = RoundedCornerShape(percent = 50)
            )
            .padding(vertical = 18.dp)
            .clickable { onClick() }
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surfaceContainerLowest,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = text.uppercase(),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            style = MaterialTheme.typography.labelMedium,
            fontSize = 14.sp,
            letterSpacing = 1.2.sp
        )
    }
}

@Composable
fun MainContent(
    basicBookDetails: BookDetailViewModel.Args,
    secondaryBookDetails: BookDetailViewModel.BookDetailsUIModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 120.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        item {
            TopBookContent(
                imageLink = basicBookDetails.bookImageUrl,
                title = basicBookDetails.bookName,
                authors = basicBookDetails.authors,
            )
        }

        when (secondaryBookDetails) {
            is BookDetailViewModel.BookDetailsUIModel.Error -> {
                item {
                    Text("Error")
                }
            }
            is BookDetailViewModel.BookDetailsUIModel.Loading -> {
                item {
                    Text("Loading...")
                }
            }
            is BookDetailViewModel.BookDetailsUIModel.Success -> {
                val bookDetails = secondaryBookDetails.bookDetails
                bookDetails.tags?.let {
                    item {
                        TagSection(
                            it, Modifier
                                .offset(y = (-2).dp)
                                .background(MaterialTheme.colorScheme.surface)
                                .fillMaxWidth()
                        )
                    }
                }

                item {
                    Spacer(Modifier.height(28.dp))
                    BookStatsSection(stats = bookDetails.stats)
                }

                item {
                    Spacer(Modifier.height(28.dp))
                    AboutBookSection(description = bookDetails.description)
                }

                item {
                    Spacer(Modifier.height(28.dp))
                    AuthorProfileSection(authors = bookDetails.authors)
                }
            }
        }
    }
}

@Composable
fun AuthorProfileSection(authors: List<Authors>) {
    val itemsToShow = 3
    val pages = authors.chunked(itemsToShow)
    var selectedAuthorIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Authors",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(pages.size) {
                    val color = if (pagerState.currentPage == it) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    }

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(color, CircleShape)
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        HorizontalPager(
            state = pagerState,
            pageSpacing = 16.dp,
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) { page ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(itemsToShow) {
                    val index = page * itemsToShow + it
                    if (index < authors.size) {
                        val author = authors[index]
                        SelectableAuthorAvatar(
                            model = author.authorPhotoUrl,
                            name = author.authorName,
                            isSelected = selectedAuthorIndex == authors.indexOf(author),
                            modifier = Modifier
                                .clickable {
                                    selectedAuthorIndex = authors.indexOf(author)
                                }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = authors[selectedAuthorIndex].authorBio,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = LightCharcoal,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun BookStatsSection(
    stats: BookStats,
    modifier: Modifier = Modifier
) {
    val bookStats = listOf(
        "Pages" to stats.pages,
        "Language" to stats.languages,
        "ISBN" to stats.isbn,
        "Published" to stats.publishedDate
    )
    val chunks = bookStats.chunked(2)
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        chunks.forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                rowItems.forEach {
                    StatsTag(data = it, modifier = Modifier.weight(1f))
                }

                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun StatsTag(
    modifier: Modifier = Modifier,
    data: Pair<String, String>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(percent = 40)
            )
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = data.first.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = data.second,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

@Composable
private fun AboutBookSection(
    description: String,
    isTextCollapsedInitial: Boolean = true
) {
    var isTextCollapsed by remember { mutableStateOf(isTextCollapsedInitial) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "About the Book",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { isTextCollapsed = !isTextCollapsed }
            ) {
                val iconRes = if (isTextCollapsed) {
                    R.drawable.outline_expand_all_24
                } else {
                    R.drawable.outline_collapse_all_24
                }
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = "Expand button",
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        }

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxWidth()
        ) {
            val maxLines = if (isTextCollapsed) 6 else Int.MAX_VALUE
            HtmlDescription(
                html = description,
                maxLines = maxLines,
                color = LightCharcoal,
                textSize = TextUnit(14f, TextUnitType.Sp),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .thenIf(isTextCollapsed, Modifier.drawTextFade())
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isTextCollapsed = !isTextCollapsed }
                .padding(vertical = 16.dp)
        ) {
            val text = if (isTextCollapsed) "Read full Synopsis " else "Hide full Synopsis "
            val icon = if (isTextCollapsed) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagSection(tags: List<String>, modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        tags.forEach {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(percent = 50)
                    )
                    .padding(vertical = 8.dp, horizontal = 24.dp)
            )
        }
    }
}

@Composable
private fun TopBookContent(
    imageLink: String?,
    title: String,
    authors: String,
    modifier: Modifier = Modifier
) {
    val surfaceColor = MaterialTheme.colorScheme.surface

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .fillMaxWidth()
            .height(420.dp)
    ) {
        BookImage(
            url = imageLink,
            shapePercent = 0,
            modifier = Modifier
                .fillMaxSize()
                .blur(10.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .alpha(1f)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            surfaceColor.copy(alpha = 0.2f),
                            surfaceColor.copy(alpha = 0.6f),
                            surfaceColor.copy(alpha = 0.6f),
                            surfaceColor.copy(alpha = 0.9f),
                            surfaceColor
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 28.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Bottom),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                BookImage(
                    url = imageLink,
                    alignment = Alignment.BottomCenter,
                    showShadow = true,
                    modifier = Modifier
                        .size(width = 150.dp, height = 240.dp)
                        .aspectRatio(2f / 3f)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = authors,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight(600)
                )
            }
        }
    }
}

@Preview(heightDp = 1400)
@Composable
private fun BookDetailPreview() {
    val authors = listOf(
        Authors(
            authorName = "Elena Vance",
            authorPhotoUrl = "https://covers.openlibrary.org/a/olid/OL23919A-L.jpg?default=false",
            authorBio = "This is a description of Elena Vance. This is just a demo description, for testing purposes. This is gonna be a little long description."
        ),
        Authors(
            authorName = "Sarah Jean",
            authorPhotoUrl = "https://covers.openlibrary.org/a/olid/OL23919A-L.jpg?default=false",
            authorBio = "This is a description of Elena Vance"
        ),
        Authors(
            authorName = "Marcus Thorne",
            authorPhotoUrl = "https://covers.openlibrary.org/a/olid/OL23919A-L.jpg?default=false",
            authorBio = "This is a description of Elena Vance"
        ),
        Authors(
            authorName = "Kiara Advani",
            authorPhotoUrl = "https://covers.openlibrary.org/a/olid/OL23919A-L.jpg?default=false",
            authorBio = "This is a description of Elena Vance"
        ),
        Authors(
            authorName = "Disha Patani",
            authorPhotoUrl = "https://covers.openlibrary.org/a/olid/OL23919A-L.jpg?default=false",
            authorBio = "This is a description of Elena Vance"
        )
    )

    val bookDetails = BookDetails(
        authors = authors,
        tags = listOf("Philosophy", "Architecture", "Mindfulness"),
        stats = BookStats(
            pages = "200",
            languages = "English",
            isbn = "1234567890",
            publishedDate = "2023"
        ),
        description = "This is a description of the book. This is just a demo description, for testing purposes. I am making the description a little longer for testing how it looks."
    )

    LibriTheme {
        MainContentScaffold(

            secondaryBookDetails = BookDetailViewModel.BookDetailsUIModel.Success(bookDetails),
            basicBookDetails = BookDetailViewModel.Args(
                bookId = "abc123",
                apiType = ApiType.UNKNOWN,
                bookName = "The Great Gatsby",
                authors = "F. Scott Fitzgerald",
                bookImageUrl = "",
                isbn13 = "123",
                isbn10 = "123"
            ),
        )
    }

}