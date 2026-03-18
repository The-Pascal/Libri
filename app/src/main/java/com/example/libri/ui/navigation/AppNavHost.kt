package com.example.libri.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.libri.domain.repository.BookRepository
import com.example.libri.ui.favorite.FavoriteScreen
import com.example.libri.ui.favorite.FavoriteViewModel
import com.example.libri.ui.home.HomeScreen
import com.example.libri.ui.home.HomeViewModel
import com.example.libri.ui.search.SearchScreen
import com.example.libri.ui.search.SearchViewModel
import com.example.libri.utils.BaseViewModelFactory

@Composable
fun AppNavHost(
    repository: BookRepository,
    navController: NavHostController,
    startDestination: Routes,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<Routes.Home> {
            val viewModel: HomeViewModel = viewModel(
                factory = BaseViewModelFactory { HomeViewModel(repository) }
            )
            HomeScreen(
                viewModel = viewModel,
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

        composable<Routes.Favorite> {
            val viewModel = viewModel<FavoriteViewModel>(
                factory = BaseViewModelFactory { FavoriteViewModel(repository) }
            )
            FavoriteScreen(viewModel = viewModel)
        }
    }
}