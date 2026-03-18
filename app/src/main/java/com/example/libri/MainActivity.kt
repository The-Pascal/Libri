package com.example.libri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.libri.data.local.AppDatabase
import com.example.libri.data.remote.Network
import com.example.libri.data.repository.BookRepositoryImpl
import com.example.libri.domain.repository.BookRepository
import com.example.libri.ui.navigation.AppNavHost
import com.example.libri.ui.navigation.Routes
import com.example.libri.ui.theme.LibriTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(this)
        val repository = BookRepositoryImpl(Network.openLibraryApi, database)

        setContent {
            LibriTheme {
                Surface {
                    AppNavigationBar(repository)
                }
            }
        }
    }
}

enum class NavigationDestination(
    val route: Routes,
    val label: String,
    val icon: ImageVector,
    val contentDesc: String
) {
    Home(Routes.Home, "Home", Icons.Default.Home, "Home"),
    Search(Routes.Search, "Search", Icons.Default.Search, "Search"),
    Favorite(Routes.Favorite, "Favorite", Icons.Default.Favorite, "Favorite")
}

@Composable
fun AppNavigationBar(
    repository: BookRepository,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier,
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                NavigationDestination.entries.forEachIndexed { _, destination ->
                    val selected = currentDestination?.hasRoute(destination.route::class) == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(route = destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.contentDesc
                            )
                        },
                        label = {
                            Text(destination.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(
            repository = repository,
            navController = navController,
            startDestination = NavigationDestination.Home.route,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        )
    }
}
