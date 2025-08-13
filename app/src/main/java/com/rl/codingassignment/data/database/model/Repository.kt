package com.rl.codingassignment.data.database.model

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
) {
    companion object {
        fun createMock(
            id: Int,
            name: String = "Repository $id",
            fullName: String = "org/repository-$id",
            description: String? = "Description for repository $id",
            htmlUrl: String = "https://github.com/org/repository-$id",
            visibility: String = "public",
            isPrivate: Boolean = false,
            ownerAvatarUrl: String = "https://avatars.githubusercontent.com/u/15876397?v=4"
        ) = Repository(
            id = id,
            name = name,
            fullName = fullName,
            description = description,
            htmlUrl = htmlUrl,
            visibility = visibility,
            isPrivate = isPrivate,
            ownerAvatarUrl = ownerAvatarUrl
        )
        
        fun createMockList(count: Int = 10, startId: Int = 1): List<Repository> =
            List(count) { index ->
                createMock(
                    id = startId + index,
                    isPrivate = index % 2 == 0
                )
            }
        
        fun createPreviewSample() = Repository(
            id = 1,
            name = "Sample Repository",
            fullName = "org/sample-repository",
            description = "This is a sample repository for demonstration purposes",
            htmlUrl = "https://github.com/org/sample-repository",
            visibility = "public",
            isPrivate = false,
            ownerAvatarUrl = "https://avatars.githubusercontent.com/u/15876397?v=4"
        )
    }
}
