package com.rl.codingassignment.data.repository

import com.rl.codingassignment.data.api.GithubApi
import com.rl.codingassignment.data.database.AppDatabase
import com.rl.codingassignment.data.database.model.Repository
import com.rl.codingassignment.data.mapper.toDbModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext

class GithubRepository(
    private val api: GithubApi,
    private val database: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val repositories: Flow<List<Repository>>
        get() = database.repositoryDao().getAll()

    suspend fun fetchRepositories(page: Int, perPage: Int): Result<Int> =
        withContext(dispatcher) {
            runCatching {
                val results = api.getRepositories(page = page, perPage = perPage)

                if (page == 1) {
                    database.repositoryDao().deleteAll()
                }
                if (results.isNotEmpty()) {
                    database.repositoryDao().insertAll(results.map { it.toDbModel() })
                }
                results.size
            }.onFailure {
                it.message
            }
        }

    fun getRepositoryById(id: Int): Flow<Repository?> =
        database.repositoryDao().getById(id).distinctUntilChanged()
}