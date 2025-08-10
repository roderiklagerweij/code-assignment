package com.rl.abnassignment.presentation.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rl.abnassignment.domain.model.RepositoryModel

@Composable
fun RepoOverviewContent(
    modifier: Modifier = Modifier,
    uiState: UiState.Content<List<RepositoryModel>>,
    onRepositoryVisible: (Int) -> Unit,
    onRepoClick: (RepositoryModel) -> Unit,
) {
    if (uiState.content.isEmpty()) {
        Text(
            text = "No repositories found.",
            modifier = Modifier.padding(16.dp)
        )
        return
    }
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {

        val repositories = uiState.content
        items(repositories.size) { index ->
            onRepositoryVisible(index)
            Text(
                text = repositories[index].name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRepoClick(repositories[index]) }
                    .padding(8.dp)
            )
        }

        item {
            if (uiState.isLoadingMore) {
                Text(
                    text = "Load more...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
@Preview
fun RepoOverviewContentPreview() {
    RepoOverviewContent(
        uiState = UiState.Content(
            listOf(
                RepositoryModel(1, "Description 1"),
                RepositoryModel(2, "Description 2")
            )
        ),
        onRepositoryVisible = {},
        onRepoClick = {}
    )
}