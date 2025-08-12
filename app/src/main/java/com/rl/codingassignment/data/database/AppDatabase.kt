package com.rl.codingassignment.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rl.codingassignment.data.database.model.Repository

@Database(entities = [Repository::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao
}
