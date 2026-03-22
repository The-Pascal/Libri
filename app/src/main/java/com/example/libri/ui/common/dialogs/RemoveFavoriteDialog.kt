package com.example.libri.ui.common.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.libri.domain.models.Book
import com.example.libri.ui.theme.LibriTheme
import com.example.libri.ui.theme.LightCharcoal
import com.example.libri.ui.theme.NotoSerifFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveFavoriteDialog(
    book: Book,
    onDismissRequest: () -> Unit,
    onRemoveFavorite: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = { onDismissRequest() },
        modifier = modifier
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Text(
                    text = "Remove from\nFavorites?",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))

                val annotatedString = buildAnnotatedString {
                    append("Are you sure you want to remove ")

                    withStyle(
                        style = SpanStyle(
                            fontFamily = NotoSerifFamily,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append("'${book.title}' ")
                    }

                    append(" from your collection?")
                }
                Text(
                    text = annotatedString,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { onRemoveFavorite(book) },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text(
                        text = "Remove",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(2.dp, LightCharcoal),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 12.sp
                    )
                }

            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun RemoveFavoriteDialogPreview() {
    LibriTheme {
        Scaffold() {
            Box(Modifier.padding(it)) {
                RemoveFavoriteDialog(
                    book = Book(
                        id = "123",
                        title = "The Great Gatsby",
                        authors = listOf("F. Scott Fitzgerald"),
                        coverUrl = "",
                        isBookmarked = true,
                        publishYear = "2001"
                    ),
                    {},
                    {}
                )
            }
        }
    }
}