package com.rl.abnassignment

import android.app.Application
import com.rl.abnassignment.data.di.dataModule
import com.rl.abnassignment.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AbnAssignmentApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AbnAssignmentApplication)
//            fragmentFactory()
            modules(
                presentationModule,
                dataModule
            )
        }
    }
}