package com.example.libri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import com.example.libri.data.remote.Network
import com.example.libri.data.repository.BookRepositoryImpl
import com.example.libri.ui.navigation.AppNavHost
import com.example.libri.ui.theme.LibriTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = BookRepositoryImpl(Network.openLibraryApi)

        setContent {
            LibriTheme {
                Surface {
                    AppNavHost(repository = repository)
                }
            }
        }
    }
}
