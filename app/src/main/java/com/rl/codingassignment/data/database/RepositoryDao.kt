package com.rl.codingassignment.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rl.codingassignment.data.database.model.Repository
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {
    @Query("SELECT * FROM Repository ORDER BY full_name ASC")
    fun getAll(): Flow<List<Repository>>

    @Query("SELECT * FROM Repository WHERE id = :id")
    fun getById(id: Int): Flow<Repository?>

    @Upsert
    suspend fun upsert(repo: Repository)

    @Upsert
    suspend fun insertAll(repos: List<Repository>)

    @Query("DELETE FROM Repository")
    fun deleteAll()
}