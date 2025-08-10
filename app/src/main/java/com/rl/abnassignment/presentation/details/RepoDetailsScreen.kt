package com.rl.abnassignment.presentation.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.rl.abnassignment.domain.model.RepositoryModel
import com.rl.abnassignment.presentation.overview.UiState
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            viewModel.uiState.collectAsState().value?.let {
                RepoDetailsContent(it)
            }
        }
    }
}
