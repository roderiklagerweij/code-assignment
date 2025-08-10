package com.rl.abnassignment.presentation.overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rl.abnassignment.data.repository.GithubRepository
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RepoOverviewViewModel(private val repository: GithubRepository): ViewModel() {

    val repositories = repository.repositories.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            repository.fetchRepositories()
        }
    }

    fun onRefresh() {
        TODO("Not yet implemented")
    }

    fun onRetry() {
        TODO("Not yet implemented")
    }
}