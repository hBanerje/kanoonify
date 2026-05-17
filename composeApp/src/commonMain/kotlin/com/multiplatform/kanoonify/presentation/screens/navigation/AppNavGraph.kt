package com.multiplatform.kanoonify.presentation.screens.navigation

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
import com.multiplatform.kanoonify.presentation.screens.screens.LandingScreen
import com.multiplatform.kanoonify.presentation.screens.screens.LawListScreen
import com.multiplatform.kanoonify.presentation.screens.screens.LawsScreen
import com.multiplatform.kanoonify.presentation.screens.screens.SplashScreen
import com.multiplatform.kanoonify.presentation.screens.screens.SubCategoryScreen
import com.multiplatform.kanoonify.presentation.screens.viewmodel.LawsViewModel
import kotlinx.serialization.Serializable

@Serializable
object SplashRoute

@Serializable
object LandingRoute

@Serializable
object AskRoute

@Serializable
object CategoriesRoute

@Serializable
object LawsRoute

@Serializable
data class SubCategoryRoute(val category: String)

@Serializable
data class LawListRoute(val title: String, val keywords: List<String>)

@Composable
fun KanoonifyRoot(driverFactory: DatabaseDriverFactory) {
    val navController = rememberNavController()

    val lawsViewModel = remember {
        val dbHelper = DatabaseHelper(driverFactory)
        val repository = LawRepository(dbHelper.database)
        LawsViewModel(repository)
    }

    NavHost(
        navController = navController,
        startDestination = SplashRoute
    ) {
        composable<SplashRoute> {
            SplashScreen(navController)
        }
        composable<LandingRoute> {
            LandingScreen(navController)
        }
        composable<AskRoute> {
            AskScreen()
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
            SubCategoryScreen(
                category = route.category,
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
            LawListScreen(subCategory = subCategory)
        }
    }
}
