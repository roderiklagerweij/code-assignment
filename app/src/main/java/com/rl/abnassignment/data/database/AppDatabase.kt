package com.rl.abnassignment.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rl.abnassignment.data.database.model.Repository

@Database(entities = [Repository::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}
