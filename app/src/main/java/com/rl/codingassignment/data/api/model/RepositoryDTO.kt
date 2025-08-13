package com.rl.codingassignment.data.api.model

import com.squareup.moshi.JsonClass
import java.util.Locale

@JsonClass(generateAdapter = true)
data class RepositoryDTO(
    val id: Int,
    val name: String,
    val full_name: String,
    val description: String?,
    val html_url: String,
    val visibility: String,
    val private: Boolean,
    val owner: OwnerDTO
) {
    companion object {
        fun createMock(
            id: Int,
            name: String = "Test Repo ${String.format(Locale.getDefault(), "%02d", id)}",
            fullName: String = "org/test-repo-${String.format(Locale.getDefault(), "%02d", id)}",
            description: String? = "Test description $id",
            htmlUrl: String = "https://github.com/org/test-repo-$id",
            visibility: String = "public",
            isPrivate: Boolean = false,
            ownerAvatarUrl: String = "https://example.com/avatar.png"
        ) = RepositoryDTO(
            id = id,
            name = name,
            full_name = fullName,
            description = description,
            html_url = htmlUrl,
            visibility = visibility,
            private = isPrivate,
            owner = OwnerDTO(avatar_url = ownerAvatarUrl)
        )
        
        fun createMockList(count: Int = 10, startId: Int = 1): List<RepositoryDTO> =
            List(count) { index ->
                createMock(id = startId + index)
            }
    }
}
