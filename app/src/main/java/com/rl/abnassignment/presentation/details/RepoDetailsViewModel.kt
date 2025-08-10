package com.rl.abnassignment.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.savedstate.savedState
import com.rl.abnassignment.DetailsScreen
import com.rl.abnassignment.data.repository.GithubRepository
import com.rl.abnassignment.presentation.overview.UiState
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onStart
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
