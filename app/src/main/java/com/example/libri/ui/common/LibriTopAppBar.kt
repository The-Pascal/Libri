package com.example.libri.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.libri.R
import com.example.libri.ui.theme.LibriTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibriTopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = "Libri",
                style = MaterialTheme.typography.headlineMedium
            )
        },
        actions = {
            IconButton(
                onClick = {}
            ) {
                Image(
                    painter = painterResource(R.drawable.demo_account_image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
        }
    )
}

@Preview
@Composable
private fun LibriTopAppBarPreview() {
    LibriTheme {
        LibriTopAppBar()
    }
}