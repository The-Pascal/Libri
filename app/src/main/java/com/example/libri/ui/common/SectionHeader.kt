package com.example.libri.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.libri.ui.theme.LibriTheme

@Composable
fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier
    )
}

@Preview
@Composable
private fun SectionHeaderPreview() {
    LibriTheme {
        Surface() {
            SectionHeader(text = "Trending Now")
        }
    }
}