package com.rl.codingassignment.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rl.codingassignment.navigation.DetailsScreen
import com.rl.codingassignment.data.repository.GithubRepository
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn

class RepoDetailsViewModel(
    private val repository: GithubRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val uiState = getUiFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = null
        )

    private fun getUiFlow() =
        repository.getRepositoryById(savedStateHandle.toRoute<DetailsScreen>().repositoryId)
}
