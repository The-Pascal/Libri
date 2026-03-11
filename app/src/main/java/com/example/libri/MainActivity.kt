package com.example.libri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.libri.data.remote.Network
import com.example.libri.data.repository.BookRepositoryImpl
import com.example.libri.ui.search.SearchScreen
import com.example.libri.ui.search.SearchViewModel
import com.example.libri.ui.theme.LibriTheme
import com.example.libri.utils.BaseViewModelFactory

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LibriTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Libre")
                            }
                        )
                    }
                ) { innerPadding ->

                    val repository = BookRepositoryImpl(Network.openLibraryApi)
                    val viewModel: SearchViewModel = viewModel(
                        factory = BaseViewModelFactory {
                            SearchViewModel(repository)
                        }
                    )

                    SearchScreen(
                        modifier = Modifier.padding(innerPadding),
                        searchViewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LibriTheme {
        Greeting("Android")
    }
}