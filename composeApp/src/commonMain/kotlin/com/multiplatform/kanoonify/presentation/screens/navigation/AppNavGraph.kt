package com.multiplatform.kanoonify.presentation.screens.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.multiplatform.kanoonify.data.LawRepository
import com.multiplatform.kanoonify.domain.model.SubCategory
import com.multiplatform.kanoonify.db.DatabaseHelper
import com.multiplatform.kanoonify.db.DatabaseDriverFactory
import com.multiplatform.kanoonify.presentation.screens.screens.AskScreen
import com.multiplatform.kanoonify.presentation.screens.screens.CategoryScreen
import com.multiplatform.kanoonify.presentation.screens.screens.COIDetailScreen
import com.multiplatform.kanoonify.presentation.screens.screens.COIScreen
import com.multiplatform.kanoonify.presentation.screens.screens.LandingScreen
import com.multiplatform.kanoonify.presentation.screens.screens.LawDetailScreen
import com.multiplatform.kanoonify.presentation.screens.screens.LawListScreen
import com.multiplatform.kanoonify.presentation.screens.screens.LawsScreen
import com.multiplatform.kanoonify.presentation.screens.screens.SplashScreen
import com.multiplatform.kanoonify.presentation.screens.screens.SubCategoryScreen
import com.multiplatform.kanoonify.presentation.screens.viewmodel.AskViewModel
import com.multiplatform.kanoonify.presentation.screens.viewmodel.COIViewModel
import com.multiplatform.kanoonify.presentation.screens.viewmodel.LawDetailViewModel
import com.multiplatform.kanoonify.presentation.screens.viewmodel.LawListViewModel
import com.multiplatform.kanoonify.presentation.screens.viewmodel.LawsViewModel
import com.multiplatform.kanoonify.presentation.screens.viewmodel.SubCategoryViewModel
import kotlinx.serialization.Serializable

@Serializable object SplashRoute
@Serializable object LandingRoute
@Serializable object AskRoute
@Serializable object CategoriesRoute
@Serializable object LawsRoute
@Serializable object CoiRoute
@Serializable data class CoiDetailRoute(val id: Int)

@Serializable data class SubCategoryRoute(val category: String)
@Serializable data class LawListRoute(val title: String, val keywords: List<String>)
@Serializable data class LawDetailRoute(val id: Int)

private const val TransitionDuration = 280

@Composable
fun KanoonifyRoot(driverFactory: DatabaseDriverFactory) {
    val navController = rememberNavController()

    val lawsViewModel = remember {
        val dbHelper = DatabaseHelper(driverFactory)
        val repository = LawRepository(dbHelper.database)
        LawsViewModel(repository)
    }

    val coiViewModel = remember { COIViewModel() }

    NavHost(
        navController = navController,
        startDestination = SplashRoute,
        enterTransition  = { fadeIn(tween(TransitionDuration)) +
            slideInHorizontally(tween(TransitionDuration)) { it / 8 } },
        exitTransition   = { fadeOut(tween(TransitionDuration)) },
        popEnterTransition = { fadeIn(tween(TransitionDuration)) },
        popExitTransition  = { fadeOut(tween(TransitionDuration)) +
            slideOutHorizontally(tween(TransitionDuration)) { it / 8 } }
    ) {
        composable<SplashRoute> {
            SplashScreen(navController)
        }
        composable<LandingRoute> {
            LandingScreen(
                onAskClick = { navController.navigate(AskRoute) },
                onBrowseLawsClick = { navController.navigate(CategoriesRoute) },
                onCoiClick = { navController.navigate(CoiRoute) }
            )
        }
        composable<CoiRoute> {
            COIScreen(
                viewModel = coiViewModel,
                onArticleClick = { article ->
                    navController.navigate(CoiDetailRoute(id = article.id))
                }
            )
        }
        composable<CoiDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CoiDetailRoute>()
            COIDetailScreen(articleId = route.id, viewModel = coiViewModel)
        }
        composable<AskRoute> {
            val viewModel = remember { AskViewModel() }
            AskScreen(viewModel = viewModel)
        }
        composable<CategoriesRoute> {
            CategoryScreen(onCategoryClick = { category ->
                navController.navigate(SubCategoryRoute(category = category))
            })
        }
        composable<LawsRoute> {
            LawsScreen(viewModel = lawsViewModel)
        }
        composable<SubCategoryRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<SubCategoryRoute>()
            val viewModel = remember(route.category) { SubCategoryViewModel(route.category) }
            SubCategoryScreen(
                viewModel = viewModel,
                onSubCategoryClick = { subCategory ->
                    navController.navigate(
                        LawListRoute(
                            title = subCategory.title,
                            keywords = subCategory.keywords
                        )
                    )
                }
            )
        }
        composable<LawListRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<LawListRoute>()
            val subCategory = SubCategory(title = route.title, keywords = route.keywords)
            val viewModel = remember(route) { LawListViewModel(subCategory) }
            LawListScreen(
                viewModel = viewModel,
                onLawClick = { law -> navController.navigate(LawDetailRoute(id = law.id)) },
                onAskAiClick = { navController.navigate(AskRoute) }
            )
        }
        composable<LawDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<LawDetailRoute>()
            val viewModel = remember(route.id) { LawDetailViewModel(route.id) }
            LawDetailScreen(
                viewModel = viewModel,
                onAskAiClick = { navController.navigate(AskRoute) }
            )
        }
    }
}
