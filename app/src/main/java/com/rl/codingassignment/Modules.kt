package com.rl.codingassignment

import com.rl.codingassignment.data.di.dataModule
import com.rl.codingassignment.presentation.di.presentationModule

fun koinModules() = listOf(
    dataModule,
    presentationModule
)