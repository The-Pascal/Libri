package com.example.libri.ui.common.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.libri.R
import com.example.libri.ui.common.BookImage
import com.example.libri.ui.theme.LibriTheme
import com.example.libri.ui.theme.LightCharcoal
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateBookProgressBottomSheet(
    bookName: String,
    bookImageUrl: String,
    currentPage: Int,
    totalBookPages: Int,
    isBookCompleted: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var currentPage by remember { mutableIntStateOf(currentPage) }
    var sliderPosition by remember { mutableFloatStateOf(currentPage.toFloat()) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isCompleted by remember { mutableStateOf(isBookCompleted) }

    val animateAndDismiss = {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissRequest()
            }
        }
    }

    ModalBottomSheet(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        sheetState = sheetState,
        onDismissRequest = {
            animateAndDismiss()
        }
    ) {
        Column(
            modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 32.dp)
        ) {
            Row() {
                BookImage(
                    url = bookImageUrl,
                    modifier = Modifier
                        .width(80.dp)
                        .aspectRatio(2f / 3f)
                )
                Column(
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = bookName,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "$totalBookPages TOTAL PAGES",
                        style = MaterialTheme.typography.bodyMedium,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(
                    text = "CURRENT PAGE",
                    style = MaterialTheme.typography.labelMedium,
                    letterSpacing = 1.2.sp,
                    fontWeight = FontWeight(800),
                    modifier = Modifier.weight(1f)
                )

                BasicTextField(
                    value = currentPage.toString(),
                    onValueChange = { currentPage = it.toIntOrNull() ?: 0 },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                shape = RoundedCornerShape(percent = 50)
                            )
                            .padding(4.dp)
                            .widthIn(min = 70.dp, max = 120.dp)
                    ) {
                        Text(
                            text = currentPage.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            maxLines = 1
                        )
                    }
                }

                Text(
                    text = " / $totalBookPages",
                    style = MaterialTheme.typography.titleMedium,
                    letterSpacing = 1.2.sp,
                    color = LightCharcoal
                )
            }

            PageSlider(
                sliderPosition,
                totalPages = totalBookPages,
                onValueChange = {
                    sliderPosition = it
                    currentPage = it.toInt()
                }
            )

            Spacer(Modifier.height(24.dp))
            MarkCompletedSection(
                isCompleted = isCompleted,
            ) {
                isCompleted = it
            }

            Spacer(Modifier.height(44.dp))
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                ) {
                    Text(
                        text = "UPDATE PROGRESS ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        letterSpacing = 1.5.sp
                    )
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Tick icon",
                        tint = MaterialTheme.colorScheme.surfaceContainerLowest,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            Text(
                text = "CANCEL & GO BACK",
                style = MaterialTheme.typography.labelMedium,
                color = LightCharcoal,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        animateAndDismiss()
                    }
                    .padding(vertical = 12.dp)
            )

        }
    }
}

@Composable
fun MarkCompletedSection(
    isCompleted: Boolean,
    onCompleteValueChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RoundedCornerShape(percent = 30)
            )
            .padding(vertical = 16.dp, horizontal = 12.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.rounded_tick_icon),
            contentDescription = "Rounded tick icon",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                    shape = CircleShape
                )
                .padding(8.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = "Mark as Completed",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight(800)
            )
            Text(
                text = "Finished the entire book",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight(400)
            )
        }

        Switch(
            checked = isCompleted,
            onCheckedChange = onCompleteValueChange
        )
    }
}

@Composable
fun PageSlider(
    sliderPosition: Float,
    onValueChange: (Float) -> Unit,
    totalPages: Int,
) {
    Slider(
        value = sliderPosition,
        onValueChange = onValueChange,
        valueRange = 0f..totalPages.toFloat(),
        modifier = Modifier.padding(top = 24.dp)
    )

    Row(
        modifier = Modifier.padding(top = 24.dp)
    ) {
        val style = MaterialTheme.typography.labelMedium.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp
        )
        val percent = (sliderPosition / totalPages * 100).toInt()
        Text(
            text = "START",
            style = style,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "$percent% COMPLETE",
            style = style
        )
        Text(
            text = "END",
            style = style,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
private fun UpdateBookProgressBottomSheetPreview() {
    LibriTheme {
        Surface() {
            UpdateBookProgressBottomSheet(
                bookName = "The Architecture of Stillness",
                bookImageUrl = "",
                totalBookPages = 184,
                currentPage = 25,
                isBookCompleted = false,
                onDismissRequest = {}
            )
        }
    }
}