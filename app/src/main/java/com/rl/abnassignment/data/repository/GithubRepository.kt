package com.rl.abnassignment.data.repository

import com.rl.abnassignment.data.client.RepositoriesAPI
import com.rl.abnassignment.data.database.AppDatabase
import com.rl.abnassignment.data.mapper.toDbModel
import com.rl.abnassignment.data.mapper.toDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GithubRepository(
    private val api: RepositoriesAPI,
    private val database: AppDatabase
) {
    val repositories
        get() = database.repoDao().getAll().map { list ->
            list.map { it.toDomainModel() }
        }

    suspend fun fetchRepositories(page: Int, perPage: Int): Result<Int> = withContext(
        Dispatchers.IO) {
        runCatching {
            val results = api.getRepositories(page = page, perPage = perPage)
            if (page == 1) {
                database.repoDao().deleteAll()
            }
            if (results.isNotEmpty()) {
                database.repoDao().insertAll(results.map { it.toDbModel() })
            }
            results.size
        }.onFailure {
            it.message
        }
    }
}