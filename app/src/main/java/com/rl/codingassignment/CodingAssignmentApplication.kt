package com.rl.codingassignment

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CodingAssignmentApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CodingAssignmentApplication)
            modules(
                koinModules()
            )
        }
    }
}