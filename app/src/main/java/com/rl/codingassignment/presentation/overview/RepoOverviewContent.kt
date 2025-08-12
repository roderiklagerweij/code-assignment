package com.rl.codingassignment.presentation.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rl.codingassignment.data.database.model.Repository

@Composable
fun RepoOverviewContent(
    modifier: Modifier = Modifier,
    uiState: UiState.Content<List<Repository>>,
    onRepositoryVisible: (Int) -> Unit,
    onRepoClick: (Int) -> Unit,
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
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        val repositories = uiState.content
        items(repositories.size) { index ->
            onRepositoryVisible(index)
            RepositoryItem(
                repository = repositories[index],
                onClick = { onRepoClick(repositories[index].id) }
            )
        }

        item {
            if (uiState.isLoadingMore) {
                Text(
                    text = "Loading more items...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
private fun RepositoryItem(
    repository: Repository,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(repository.ownerAvatarUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Owner avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = repository.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = if (repository.isPrivate) "Private" else "Public",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (repository.isPrivate) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                    
                    Text(
                        text = "• ${repository.visibility}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun RepoOverviewContentPreview() {
    RepoOverviewContent(
        uiState = UiState.Content(
            Repository.createMockList(count = 5)
        ),
        onRepositoryVisible = {},
        onRepoClick = {}
    )
}