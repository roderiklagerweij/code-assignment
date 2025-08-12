package com.rl.abnassignment.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Repository(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "html_url") val htmlUrl: String,
    @ColumnInfo(name = "visibility") val visibility: String,
    @ColumnInfo(name = "private") val isPrivate: Boolean,
    @ColumnInfo(name = "owner_avatar_url") val ownerAvatarUrl: String
)
