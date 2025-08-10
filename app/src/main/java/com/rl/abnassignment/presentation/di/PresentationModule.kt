package com.rl.abnassignment.presentation.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.rl.abnassignment.presentation.details.RepoDetailsViewModel
import com.rl.abnassignment.presentation.overview.RepoOverviewViewModel

val presentationModule = module {
    viewModelOf(::RepoOverviewViewModel)
    viewModelOf(::RepoDetailsViewModel)

}