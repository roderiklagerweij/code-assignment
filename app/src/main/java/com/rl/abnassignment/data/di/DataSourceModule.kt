package com.rl.abnassignment.data.di


import androidx.room.Room
import com.rl.abnassignment.data.client.GithubApi
import com.rl.abnassignment.data.database.AppDatabase
import com.rl.abnassignment.data.repository.GithubRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val dataModule = module {
    singleOf(::GithubRepository)
    single {
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(get())
            .build()
            .create(GithubApi::class.java)
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "database-name"
        ).build()
    }
}
