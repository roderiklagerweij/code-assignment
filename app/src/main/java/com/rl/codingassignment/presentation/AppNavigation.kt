package com.rl.codingassignment.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rl.codingassignment.presentation.details.RepoDetailsScreen
import com.rl.codingassignment.presentation.overview.RepoOverviewScreen
import kotlinx.serialization.Serializable

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = OverviewScreen,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<OverviewScreen> {
            RepoOverviewScreen(
                onRepoClick = { repositoryId ->
                    navController.navigate(DetailsScreen(repositoryId))
                },
            )
        }
        
        composable<DetailsScreen> {
            RepoDetailsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

@Serializable
data object OverviewScreen

@Serializable
data class DetailsScreen(val repositoryId: Int)