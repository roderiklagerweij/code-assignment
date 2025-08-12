package com.rl.codingassignment.data.mapper

import com.rl.codingassignment.data.api.model.RepositoryDTO
import com.rl.codingassignment.data.database.model.Repository

fun RepositoryDTO.toDbModel(): Repository {
    return Repository(
        id = this.id,
        name = this.name,
        fullName = this.full_name,
        description = this.description,
        htmlUrl = this.html_url,
        visibility = this.visibility,
        isPrivate = this.private,
        ownerAvatarUrl = this.owner.avatar_url
    )
}

