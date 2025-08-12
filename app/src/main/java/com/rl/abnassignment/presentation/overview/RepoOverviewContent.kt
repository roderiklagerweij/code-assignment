package com.rl.abnassignment.presentation.overview

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rl.abnassignment.R
import com.rl.abnassignment.domain.model.RepositoryModel

@Composable
fun RepoOverviewContent(
    modifier: Modifier = Modifier,
    uiState: UiState.Content<List<RepositoryModel>>,
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
    repository: RepositoryModel,
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
            listOf(
                RepositoryModel(
                    id = 1,
                    name = "Repository 1",
                    fullName = "org/repository-1",
                    description = "Sample description",
                    htmlUrl = "https://github.com/org/repo1",
                    visibility = "public",
                    isPrivate = false,
                    ownerAvatarUrl = "https://example.com/avatar.png"
                ),
                RepositoryModel(
                    id = 2,
                    name = "Repository 2",
                    fullName = "org/repository-2",
                    description = "Another description",
                    htmlUrl = "https://github.com/org/repo2",
                    visibility = "private",
                    isPrivate = true,
                    ownerAvatarUrl = "https://example.com/avatar.png"
                )
            )
        ),
        onRepositoryVisible = {},
        onRepoClick = {}
    )
}