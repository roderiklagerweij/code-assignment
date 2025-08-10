package com.rl.abnassignment.presentation.details

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.rl.abnassignment.domain.model.RepositoryModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailsScreen(repositoryId: String, viewModel: RepoDetailsViewModel = koinViewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                {
                    Text("Repository Details")
                },
            )
        }
    ) { innerPadding ->

    }
}

