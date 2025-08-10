package com.rl.abnassignment.presentation.details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rl.abnassignment.domain.model.RepositoryModel

@Composable
fun RepoDetailsContent(repository: RepositoryModel) {
    Text(repository.name)
}

@Composable
@Preview
fun RepoDetailsContentPreview() {
    RepoDetailsContent(
        repository = RepositoryModel(
            id = 1,
            name = "Sample Repository"
        )
    )
}