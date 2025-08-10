package com.rl.abnassignment.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Repository(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String
)
