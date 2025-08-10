package com.rl.abnassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rl.abnassignment.presentation.details.RepoDetailsScreen
import com.rl.abnassignment.presentation.overview.RepoOverviewScreen
import com.rl.abnassignment.ui.theme.ABNAssignmentTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ABNAssignmentTheme {
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
                        RepoDetailsScreen(it.id)
                    }
                }
            }
        }
    }
}

@Serializable
data object OverviewScreen

@Serializable
data class DetailsScreen(val repositoryId: Int)
