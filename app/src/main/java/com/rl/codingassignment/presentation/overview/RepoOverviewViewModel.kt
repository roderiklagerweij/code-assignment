package com.rl.codingassignment.presentation.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rl.codingassignment.data.repository.GithubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
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

private const val PAGE_SIZE = 10

class RepoOverviewViewModel(private val repository: GithubRepository) : ViewModel() {

    private val initialLoadState = MutableStateFlow<LoadState>(LoadState.Loading)
    private val loadingMoreState = MutableStateFlow<LoadState>(LoadState.NotLoading)
    private val endReachedState = MutableStateFlow(false)

    val uiState = getUiFlow()
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = UiState.Loading
        )

    private fun getUiFlow() =
        combine(
            repository.repositories,
            initialLoadState,
            loadingMoreState,
            endReachedState
        ) { repos, loadState, loadMoreState, endReachedState ->
            if (loadState is LoadState.Error && repos.isEmpty()) {
                UiState.Error(loadState.message)
            } else if (repos.isEmpty()) {
                UiState.Loading
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
        endReachedState.value = false
        initialLoadState.value = LoadState.Loading
        val result = repository.fetchRepositories(page = 1, perPage = PAGE_SIZE)
        initialLoadState.value = result.toLoadState()
    }

    fun onRepositoryVisible(index: Int) {
        viewModelScope.launch {
            if (loadingMoreState.value is LoadState.Loading) return@launch
            if (endReachedState.value) return@launch

            val repos = repository.repositories.first()
            if (repos.isEmpty()) return@launch

            if (index == repos.size - 1) {
                val page = (repos.size / PAGE_SIZE) + 1
                loadingMoreState.value = LoadState.Loading
                val result = repository.fetchRepositories(page, perPage = PAGE_SIZE)

                if (result.isFailure) {
                    // Avoid loading loops
                    endReachedState.value = true
                }
                result.getOrNull()?.let {
                    // If the result is less than PAGE_SIZE, we assume we've reached the end
                    if (it < PAGE_SIZE) {
                        endReachedState.value = true
                    }
                }
                loadingMoreState.value = result.toLoadState()
            }
        }
    }

    fun onRetry() = refresh()
}