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

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Content<T>(val content: T, val isLoadingMore: Boolean = false) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class RepoOverviewViewModel(private val repository: GithubRepository) : ViewModel() {

    private val isLoadingMore = MutableStateFlow(false)

    val uiState = getUiFlow()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    private fun getUiFlow() =
        combine(
            repository.repositories,
            isLoadingMore
        ) { repos, loadingMore ->
            UiState.Content(content = repos, isLoadingMore = loadingMore)
        }.onStart {
            repository.fetchRepositories(page = 1, perPage = 10)
        }

    fun onRepositoryVisible(index: Int) {
        viewModelScope.launch {
            if (isLoadingMore.value) return@launch

            val repos = repository.repositories.first()
            if (repos.isEmpty()) return@launch

            if (index == repos.size - 1) {
                val page = (repos.size / 10) + 1
                isLoadingMore.value = true
                repository.fetchRepositories(page, perPage = 10)
                isLoadingMore.value = false
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            isLoadingMore.value = false
            repository.fetchRepositories(page = 1, perPage = 10)
        }
    }

    fun onRetry() {
        viewModelScope.launch {
            isLoadingMore.value = false
            repository.fetchRepositories(page = 1, perPage = 10)
        }
    }
}