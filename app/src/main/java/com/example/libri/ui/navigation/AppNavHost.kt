package com.example.libri.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.libri.domain.repository.BookRepository
import com.example.libri.ui.details.BookDetailScreen
import com.example.libri.ui.details.BookDetailViewModel
import com.example.libri.ui.favorite.FavoriteScreen
import com.example.libri.ui.favorite.FavoriteViewModel
import com.example.libri.ui.home.HomeScreen
import com.example.libri.ui.home.HomeViewModel
import com.example.libri.ui.search.SearchScreen
import com.example.libri.ui.search.SearchViewModel
import com.example.libri.utils.ApiType
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
        modifier = Modifier.fillMaxSize()
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
                navigateToBookDetails = {
                    navController.navigate(
                        Routes.BookDetail(
                            bookId = it.id,
                            apiType = it.apiType.name,
                            bookName = it.title,
                            authors = it.authors.joinToString(),
                            bookImageUrl = it.imageLinks?.large ?: ""
                        )
                    )
                },
                modifier = modifier
            )
        }

        composable<Routes.Search> {
            val viewModel: SearchViewModel = viewModel(
                factory = BaseViewModelFactory { SearchViewModel(repository) }
            )

            SearchScreen(
                searchViewModel = viewModel,
                modifier = modifier
            )
        }

        composable<Routes.Favorite> {
            val viewModel = viewModel<FavoriteViewModel>(
                factory = BaseViewModelFactory { FavoriteViewModel(repository) }
            )
            FavoriteScreen(
                viewModel = viewModel,
                modifier = modifier
            )
        }

        composable<Routes.BookDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.BookDetail>()

            val args = BookDetailViewModel.Args(
                bookId = route.bookId,
                apiType = ApiType.valueOf(route.apiType),
                bookName = route.bookName,
                authors = route.authors,
                bookImageUrl = route.bookImageUrl
            )

            val viewModel: BookDetailViewModel = viewModel(
                factory = BaseViewModelFactory { BookDetailViewModel(args = args) }
            )
            BookDetailScreen(
                viewModel = viewModel,
                modifier = modifier
            )
        }
    }
}