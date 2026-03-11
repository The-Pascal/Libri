package com.example.libri.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.libri.data.repository.BookRepositoryImpl
import com.example.libri.ui.home.HomeScreen
import com.example.libri.ui.search.SearchScreen
import com.example.libri.ui.search.SearchViewModel
import com.example.libri.utils.BaseViewModelFactory

@Composable
fun AppNavHost(repository: BookRepositoryImpl) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Home) {
        composable<Routes.Home> {
            HomeScreen(
                navigateToSearchScreen = {
                    navController.navigate(Routes.Search)
                },
            )
        }

        composable<Routes.Search> {
            val viewModel: SearchViewModel = viewModel(
                factory = BaseViewModelFactory { SearchViewModel(repository) }
            )

            SearchScreen(
                searchViewModel = viewModel
            )
        }
    }
}