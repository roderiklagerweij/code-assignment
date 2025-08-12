package com.rl.codingassignment.presentation.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.rl.codingassignment.presentation.details.RepoDetailsViewModel
import com.rl.codingassignment.presentation.overview.RepoOverviewViewModel

val presentationModule = module {
    viewModelOf(::RepoOverviewViewModel)
    viewModelOf(::RepoDetailsViewModel)

}