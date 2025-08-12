package com.rl.abnassignment

import com.rl.abnassignment.data.di.dataModule
import com.rl.abnassignment.presentation.di.presentationModule

fun koinModules() = listOf(
    dataModule,
    presentationModule
)