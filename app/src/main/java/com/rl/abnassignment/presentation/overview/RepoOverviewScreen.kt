package com.rl.abnassignment.presentation.overview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.rl.abnassignment.presentation.common.ErrorPage
import com.rl.abnassignment.presentation.common.LoadingPage
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoOverviewScreen(
    onRepoClick: (Int) -> Unit,
    viewModel: RepoOverviewViewModel = koinViewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                {
                    Text("Repositories")
                },
            )
        }
    ) { innerPadding ->
        val uiState = viewModel.uiState.collectAsState().value
        when (uiState) {
            is UiState.Content -> {
                RepoOverviewContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    uiState = uiState,
                    onRepoClick = onRepoClick,
                    onRepositoryVisible = viewModel::onRepositoryVisible
                )
            }

            is UiState.Error -> ErrorPage(uiState, viewModel::onRetry)
            UiState.Loading -> LoadingPage()
        }
    }
}

