package com.example.libri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
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
        val repository = BookRepositoryImpl(
            Network.googleBooksApi, Network.nytBooksApi, Network.gutendexApi, database
        )

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
    val icon: Int,
    val contentDesc: String
) {
    Discover(Routes.Home, "Discover", R.drawable.discover_outline_icon, "Home"),
    Favorite(Routes.Favorite, "Favorite", R.drawable.favorite_outline_icon, "Favorite"),
    Activity(Routes.Search, "Activity", R.drawable.activity_outline_icon, "Activity"),
    Profile(Routes.Profile, "Profile", R.drawable.profile_outline_icon, "Profile")
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
            NavigationBar(currentDestination, navController)
        }
    ) { innerPadding ->
        AppNavHost(
            repository = repository,
            navController = navController,
            startDestination = NavigationDestination.Discover.route,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        )
    }
}

@Composable
private fun NavigationBar(
    currentDestination: NavDestination?,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .shadow(12.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        NavigationDestination.entries.forEachIndexed { _, destination ->
            val selected = currentDestination?.hasRoute(destination.route::class) == true
            NavigationBarItem(selected, navController, destination)
        }
    }
}

@Composable
private fun RowScope.NavigationBarItem(
    isSelected: Boolean,
    navController: NavHostController,
    destination: NavigationDestination,
) {
    NavigationBarItem(
        selected = isSelected,
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
                painter = painterResource(destination.icon),
                contentDescription = destination.contentDesc
            )
        },
        label = {
            Text(
                text = destination.label,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.secondary,
            selectedIconColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    )
}

@Preview(showSystemUi = true)
@Composable
private fun NavigationBarPreview() {
    LibriTheme {
        Scaffold(
            bottomBar = {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                NavigationBar(
                    currentDestination = currentDestination,
                    navController = navController,
                    modifier = Modifier.shadow(48.dp)
                )
            }
        ) {
            Box(modifier = Modifier.padding(it))
        }
    }
}
