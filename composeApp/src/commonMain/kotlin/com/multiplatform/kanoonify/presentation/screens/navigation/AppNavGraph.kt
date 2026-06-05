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
import com.multiplatform.kanoonify.BuildSecrets
import com.multiplatform.kanoonify.data.LawRepository
import com.multiplatform.kanoonify.domain.model.SubCategory
import com.multiplatform.kanoonify.db.DatabaseHelper
import com.multiplatform.kanoonify.db.DatabaseDriverFactory
import com.multiplatform.kanoonify.news.data.datasource.RemoteNewsDataSource
import com.multiplatform.kanoonify.news.data.datasource.SampleNewsDataSource
import com.multiplatform.kanoonify.news.data.local.NewsCache
import com.multiplatform.kanoonify.news.data.remote.NewsApiService
import com.multiplatform.kanoonify.news.data.repository.NewsRepository
import com.multiplatform.kanoonify.news.platform.UrlOpener
import com.multiplatform.kanoonify.news.presentation.screens.NewsDetailScreen
import com.multiplatform.kanoonify.news.presentation.screens.NewsFeedScreen
import com.multiplatform.kanoonify.news.presentation.screens.NewsSearchScreen
import com.multiplatform.kanoonify.news.presentation.viewmodel.NewsViewModel
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

@Serializable object NewsRoute
@Serializable object NewsSearchRoute
@Serializable data class NewsDetailRoute(val articleId: String)

@Serializable data class SubCategoryRoute(val category: String)

@Serializable data class LawListRoute(val payload: String) {
    companion object {
        fun of(title: String, keywords: List<String>): LawListRoute =
            LawListRoute(payload = SubCategoryPayload(title, keywords).encode())
    }
    fun decode(): SubCategoryPayload = SubCategoryPayload.decode(payload)
}

@Serializable
data class SubCategoryPayload(val title: String, val keywords: List<String>) {
    @OptIn(kotlin.io.encoding.ExperimentalEncodingApi::class)
    fun encode(): String {
        val json = kotlinx.serialization.json.Json.encodeToString(serializer(), this)
        return kotlin.io.encoding.Base64.UrlSafe.encode(json.encodeToByteArray())
    }
    companion object {
        @OptIn(kotlin.io.encoding.ExperimentalEncodingApi::class)
        fun decode(payload: String): SubCategoryPayload {
            val bytes = kotlin.io.encoding.Base64.UrlSafe.decode(payload)
            val json = bytes.decodeToString()
            return kotlinx.serialization.json.Json.decodeFromString(serializer(), json)
        }
    }
}

@Serializable data class LawDetailRoute(val id: Int)

private const val TransitionDuration = 280

@Composable
fun KanoonifyRoot(driverFactory: DatabaseDriverFactory) {
    val navController = rememberNavController()

    val dbHelper = remember { DatabaseHelper(driverFactory) }

    val lawsViewModel = remember {
        val repository = LawRepository(dbHelper.database)
        LawsViewModel(repository)
    }

    val coiViewModel = remember { COIViewModel() }

    val newsRepository = remember {
        val cache  = NewsCache(dbHelper.database)
        val sample = SampleNewsDataSource()
        val primary = if (BuildSecrets.NEWS_API_KEY.isNotBlank()) {
            RemoteNewsDataSource(NewsApiService(apiKey = BuildSecrets.NEWS_API_KEY))
        } else {
            sample
        }
        NewsRepository(primary = primary, fallback = sample, cache = cache)
    }
    val newsViewModel = remember { NewsViewModel(newsRepository) }
    val urlOpener = remember { UrlOpener() }

    val searchViewModel  = remember { SearchViewModel() }
    val savedViewModel   = remember { SavedViewModel(newsRepository) }
    val profileViewModel = remember { ProfileViewModel() }

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
                onNewsTabClick = { navController.switchTab(NewsRoute) },
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
                onNewsTabClick = { navController.switchTab(NewsRoute) },
                onSavedTabClick = { navController.switchTab(SavedRoute) },
                onProfileTabClick = { navController.switchTab(ProfileRoute) }
            )
        }
        composable<SavedRoute> {
            SavedScreen(
                viewModel = savedViewModel,
                onItemClick = { item ->

                    if (item.id.startsWith("news:")) {
                        val articleId = item.id.removePrefix("news:")
                        navController.navigate(NewsDetailRoute(articleId = articleId))
                    }
                },
                onExploreClick = { navController.switchTab(LandingRoute) },
                onAskClick = { navController.navigate(AskRoute) },
                onHomeTabClick = { navController.switchTab(LandingRoute) },
                onNewsTabClick = { navController.switchTab(NewsRoute) },
                onProfileTabClick = { navController.switchTab(ProfileRoute) }
            )
        }
        composable<ProfileRoute> {
            ProfileScreen(
                viewModel = profileViewModel,
                onAskClick = { navController.navigate(AskRoute) },
                onHomeTabClick = { navController.switchTab(LandingRoute) },
                onNewsTabClick = { navController.switchTab(NewsRoute) },
                onSavedTabClick = { navController.switchTab(SavedRoute) }
            )
        }

        composable<NewsRoute> {
            NewsFeedScreen(
                viewModel = newsViewModel,
                onArticleClick = { articleId ->
                    navController.navigate(NewsDetailRoute(articleId = articleId))
                },
                onShareText = { text, title -> urlOpener.shareText(text, title) },
                onOpenExternal = { url -> urlOpener.openUrl(url) },
                onSearchClick = { navController.navigate(NewsSearchRoute) },
                onAskClick = { navController.navigate(AskRoute) },
                onHomeTabClick = { navController.switchTab(LandingRoute) },
                onSavedTabClick = { navController.switchTab(SavedRoute) },
                onProfileTabClick = { navController.switchTab(ProfileRoute) }
            )
        }
        composable<NewsDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<NewsDetailRoute>()
            NewsDetailScreen(
                viewModel = newsViewModel,
                articleId = route.articleId,
                onBack = { navController.popBackStack() },
                onShareText = { text, title -> urlOpener.shareText(text, title) },
                onOpenExternal = { url -> urlOpener.openUrl(url) }
            )
        }
        composable<NewsSearchRoute> {
            NewsSearchScreen(
                viewModel = newsViewModel,
                onBack = { navController.popBackStack() },
                onArticleClick = { articleId ->
                    navController.navigate(NewsDetailRoute(articleId = articleId))
                },
                onShareText = { text, title -> urlOpener.shareText(text, title) },
                onOpenExternal = { url -> urlOpener.openUrl(url) }
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
                        LawListRoute.of(
                            title = subCategory.title,
                            keywords = subCategory.keywords
                        )
                    )
                }
            )
        }
        composable<LawListRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<LawListRoute>()
            val payload = route.decode()
            val subCategory = SubCategory(title = payload.title, keywords = payload.keywords)
            val viewModel = remember(route.payload) { LawListViewModel(subCategory) }
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
