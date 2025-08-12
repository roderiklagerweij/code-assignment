package com.rl.abnassignment.data.client

import com.rl.abnassignment.data.client.model.RepositoryDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @GET("users/{user}/repos")
    suspend fun getRepositories(
        @Path("user") user: String = "abnamrocoesd",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
        @Query("sort") sort: String = "full_name",
    ): List<RepositoryDTO>
}