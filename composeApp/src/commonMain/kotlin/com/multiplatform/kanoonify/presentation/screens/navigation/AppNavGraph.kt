package com.multiplatform.kanoonify.presentation.screens.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
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
import com.multiplatform.kanoonify.presentation.screens.screens.LawyerChatScreen
import com.multiplatform.kanoonify.presentation.screens.screens.LawyerListScreen
import com.multiplatform.kanoonify.presentation.screens.screens.LawyerProfileScreen
import com.multiplatform.kanoonify.presentation.screens.screens.ProfileScreen
import com.multiplatform.kanoonify.presentation.screens.screens.SavedScreen
import com.multiplatform.kanoonify.presentation.screens.screens.SearchScreen
import com.multiplatform.kanoonify.presentation.screens.screens.SplashScreen
import com.multiplatform.kanoonify.presentation.screens.screens.SubCategoryScreen
import com.multiplatform.kanoonify.presentation.screens.viewmodel.AskViewModel
import com.multiplatform.kanoonify.presentation.screens.viewmodel.COIViewModel
import com.multiplatform.kanoonify.presentation.screens.viewmodel.LawDetailViewModel
import com.multiplatform.kanoonify.presentation.screens.viewmodel.LawListViewModel
import com.multiplatform.kanoonify.presentation.screens.viewmodel.LawsViewModel
import com.multiplatform.kanoonify.presentation.screens.viewmodel.LawyerAccessViewModel
import com.multiplatform.kanoonify.presentation.screens.viewmodel.LawyerChatViewModel
import com.multiplatform.kanoonify.presentation.screens.viewmodel.LawyerListViewModel
import com.multiplatform.kanoonify.presentation.screens.viewmodel.ProfileViewModel
import com.multiplatform.kanoonify.presentation.screens.viewmodel.SavedViewModel
import com.multiplatform.kanoonify.presentation.screens.viewmodel.SearchViewModel
import com.multiplatform.kanoonify.presentation.screens.viewmodel.SubCategoryViewModel
import kotlinx.serialization.Serializable

@Serializable object SplashRoute
@Serializable object LandingRoute
@Serializable object AskRoute
@Serializable object CategoriesRoute
@Serializable object LawsRoute
@Serializable object CoiRoute
@Serializable data class CoiDetailRoute(val id: Int)
@Serializable object LawyersRoute
@Serializable data class LawyerProfileRoute(val lawyerId: String)
@Serializable data class LawyerChatRoute(val lawyerId: String)

@Serializable object SearchRoute
@Serializable object SavedRoute
@Serializable object ProfileRoute

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

    // Bottom-tab VMs survive cross-tab navigation so user state (search history,
    // bookmarks, settings toggles) persists while the user moves around.
    val searchViewModel  = remember { SearchViewModel() }
    val savedViewModel   = remember { SavedViewModel() }
    val profileViewModel = remember { ProfileViewModel() }

    // Bottom-tab switcher — singleTop + state-preserving popUpTo so we don't
    // stack identical screens when the user toggles tabs.
    fun NavController.switchTab(route: Any) {
        navigate(route) {
            popUpTo(graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

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
                onCoiClick = { navController.navigate(CoiRoute) },
                onConsultLawyerClick = { navController.navigate(LawyersRoute) },
                onSearchTabClick = { navController.switchTab(SearchRoute) },
                onSavedTabClick = { navController.switchTab(SavedRoute) },
                onProfileTabClick = { navController.switchTab(ProfileRoute) }
            )
        }
        composable<SearchRoute> {
            SearchScreen(
                viewModel = searchViewModel,
                onAskClick = { _ -> navController.navigate(AskRoute) },
                onBrowseLawsClick = { navController.navigate(CategoriesRoute) },
                onCoiClick = { navController.navigate(CoiRoute) },
                onConsultLawyerClick = { navController.navigate(LawyersRoute) },
                onEmergencyClick = { navController.navigate(AskRoute) },
                onHomeTabClick = { navController.switchTab(LandingRoute) },
                onSavedTabClick = { navController.switchTab(SavedRoute) },
                onProfileTabClick = { navController.switchTab(ProfileRoute) }
            )
        }
        composable<SavedRoute> {
            SavedScreen(
                viewModel = savedViewModel,
                onItemClick = { /* future: route by item.type */ },
                onExploreClick = { navController.switchTab(LandingRoute) },
                onAskClick = { navController.navigate(AskRoute) },
                onHomeTabClick = { navController.switchTab(LandingRoute) },
                onSearchTabClick = { navController.switchTab(SearchRoute) },
                onProfileTabClick = { navController.switchTab(ProfileRoute) }
            )
        }
        composable<ProfileRoute> {
            ProfileScreen(
                viewModel = profileViewModel,
                onAskClick = { navController.navigate(AskRoute) },
                onHomeTabClick = { navController.switchTab(LandingRoute) },
                onSearchTabClick = { navController.switchTab(SearchRoute) },
                onSavedTabClick = { navController.switchTab(SavedRoute) }
            )
        }
        composable<LawyersRoute> {
            val viewModel = remember { LawyerListViewModel() }
            LawyerListScreen(
                viewModel = viewModel,
                onLawyerClick = { lawyer ->
                    navController.navigate(LawyerProfileRoute(lawyerId = lawyer.id))
                }
            )
        }
        composable<LawyerProfileRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<LawyerProfileRoute>()
            // Biometric-gate VM is scoped to this route entry only — disposed
            // on leave so authentication state never crosses screen boundaries.
            val accessViewModel = remember(route.lawyerId) { LawyerAccessViewModel() }
            DisposableEffect(accessViewModel) {
                onDispose { accessViewModel.dispose() }
            }
            LawyerProfileScreen(
                lawyerId = route.lawyerId,
                accessViewModel = accessViewModel,
                onChatClick = { lawyer ->
                    navController.navigate(LawyerChatRoute(lawyerId = lawyer.id))
                }
            )
        }
        composable<LawyerChatRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<LawyerChatRoute>()
            val viewModel = remember(route.lawyerId) { LawyerChatViewModel(route.lawyerId) }
            LawyerChatScreen(viewModel = viewModel)
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
