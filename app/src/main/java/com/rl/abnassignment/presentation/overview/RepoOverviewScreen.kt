package com.rl.abnassignment.presentation.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rl.abnassignment.domain.model.RepositoryModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RepoOverviewScreen(viewModel: RepoOverviewViewModel = koinViewModel()) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        RepoOverviewContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            repositories = viewModel.repositories.collectAsState(initial = emptyList()).value,
            onRepoClick = {},
            onRefresh = viewModel::onRefresh,
            onRetry = viewModel::onRetry
        )
    }
}

@Composable
fun RepoOverviewContent(
    modifier: Modifier = Modifier,
    repositories: List<RepositoryModel>,
    onRepoClick: (RepositoryModel) -> Unit,
    onRefresh: () -> Unit,
    onRetry: () -> Unit
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        items(repositories.size) { repo ->
            Text(
                text = repositories[repo].name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRepoClick(repositories[repo]) }
                    .padding(8.dp)
            )
        }
    }
}
