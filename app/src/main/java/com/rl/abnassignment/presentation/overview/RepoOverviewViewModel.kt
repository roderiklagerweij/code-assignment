package com.rl.abnassignment.presentation.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rl.abnassignment.data.repository.GithubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface LoadState {
    data object NotLoading : LoadState
    data object Loading : LoadState
    data class Error(val message: String) : LoadState
}

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Content<T>(val content: T, val isLoadingMore: Boolean = false) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

fun Result<*>.toLoadState(): LoadState =
    if (isSuccess) {
        LoadState.NotLoading
    } else {
        LoadState.Error(exceptionOrNull()?.message ?: "Unknown error")
    }

class RepoOverviewViewModel(private val repository: GithubRepository) : ViewModel() {

    private val initialLoadState = MutableStateFlow<LoadState>(LoadState.Loading)
    private val loadingMoreState = MutableStateFlow<LoadState>(LoadState.NotLoading)

    val uiState = getUiFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = UiState.Loading
        )

    private fun getUiFlow() =
        combine(
            repository.repositories,
            initialLoadState,
            loadingMoreState
        ) { repos, loadState, loadMoreState ->
            if (loadState is LoadState.Error) {
                UiState.Error(loadState.message)
            } else {
                UiState.Content(
                    content = repos,
                    isLoadingMore = loadMoreState is LoadState.Loading
                )
            }
        }.onStart {
            refresh()
        }

    private fun refresh() = viewModelScope.launch {
        initialLoadState.value = LoadState.Loading
        val result = repository.fetchRepositories(page = 1, perPage = 10)
        initialLoadState.value = result.toLoadState()
    }

    fun onRepositoryVisible(index: Int) {
        viewModelScope.launch {
            if (loadingMoreState.value is LoadState.Loading) return@launch

            val repos = repository.repositories.first()
            if (repos.isEmpty()) return@launch

            if (index == repos.size - 1) {
                val page = (repos.size / 10) + 1
                loadingMoreState.value = LoadState.Loading
                val result = repository.fetchRepositories(page, perPage = 10)
                loadingMoreState.value = result.toLoadState()
            }
        }
    }

    fun onRetry() = refresh()
}