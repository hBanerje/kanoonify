package com.multiplatform.kanoonify.presentation.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.multiplatform.kanoonify.data.LawRepository
import com.multiplatform.kanoonify.db.DatabaseHelper
import com.multiplatform.kanoonify.db.DatabaseDriverFactory
import com.multiplatform.kanoonify.presentation.screens.screens.AskScreen
import com.multiplatform.kanoonify.presentation.screens.screens.CategoryScreen
import com.multiplatform.kanoonify.presentation.screens.screens.LandingScreen
import com.multiplatform.kanoonify.presentation.screens.screens.LawsScreen
import com.multiplatform.kanoonify.presentation.screens.screens.SplashScreen
import com.multiplatform.kanoonify.presentation.screens.viewmodel.LawsViewModel

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
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("landing") {
            LandingScreen(navController)
        }
        composable("ask") {
            AskScreen()
        }
        composable("categories") {
            CategoryScreen()
        }
        composable("laws") {
            LawsScreen(viewModel = lawsViewModel)
        }
    }
}
