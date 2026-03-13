package com.example.libri.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.libri.ui.common.shimmer.shimmerBrush

@Composable
fun ShortBookItemShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .width(120.dp)
            .padding(bottom = 8.dp)
    ) {
        val brush = shimmerBrush()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .background(brush)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val modifierText = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .padding(bottom = 4.dp)


        Box(
            modifier = modifierText
                .padding(end = 8.dp)
                .background(brush)
        )

        Box(
            modifier = modifierText
                .padding(end = 12.dp)
                .background(brush)
        )
    }
}

@Preview(showBackground = true, backgroundColor = android.graphics.Color.WHITE.toLong())
@Composable
private fun ShortBookItemShimmerPreview() {
    ShortBookItemShimmer()
}