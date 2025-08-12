package com.rl.abnassignment.data.repository

import com.rl.abnassignment.data.client.GithubApi
import com.rl.abnassignment.data.database.AppDatabase
import com.rl.abnassignment.data.mapper.toDbModel
import com.rl.abnassignment.data.mapper.toDomainModel
import com.rl.abnassignment.domain.model.RepositoryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GithubRepository(
    private val api: GithubApi,
    private val database: AppDatabase
) {
    val repositories
        get() = database.repositoryDao().getAll().map { list ->
            list.map { it.toDomainModel() }
        }
//            .filter { it.isNotEmpty() }
//            .distinctUntilChanged()

    suspend fun fetchRepositories(page: Int, perPage: Int): Result<Int> =
        withContext(Dispatchers.IO) {
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

    fun getRepositoryById(id: Int): Flow<RepositoryModel?> =
        database.repositoryDao().getById(id).map { it?.toDomainModel() }.distinctUntilChanged()
}