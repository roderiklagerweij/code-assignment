package com.rl.abnassignment.data.client

import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RepositoriesAPI {
    @GET("users/{user}/repos")
    suspend fun getRepositories(
        @Path("user") user: String = "abnamrocoesd",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
//        @Query("sort") sort: String = "created",
    ): List<RepositoryDTO>

}

@JsonClass(generateAdapter = true)
data class RepositoryDTO(
    val id: Int,
    val name: String
)