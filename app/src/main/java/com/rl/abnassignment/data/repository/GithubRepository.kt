package com.rl.abnassignment.data.repository

import com.rl.abnassignment.data.client.RepositoriesAPI
import com.rl.abnassignment.data.database.AppDatabase
import com.rl.abnassignment.data.mapper.toDbModel
import com.rl.abnassignment.data.mapper.toDomainModel
import com.rl.abnassignment.domain.model.RepositoryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GithubRepository(
    private val api: RepositoriesAPI,
    private val database: AppDatabase
) {
    val repositories
        get() = database.repoDao().getAll().map { list ->
            list.map { it.toDomainModel() }
        }

    suspend fun fetchRepositories(): Flow<List<RepositoryModel>> {
        val results = api.getRepositories(page = 1)
        if (results.isNotEmpty()) {
            database.repoDao().insertAll(results.map { it.toDbModel() })
        }
        return repositories
    }

}