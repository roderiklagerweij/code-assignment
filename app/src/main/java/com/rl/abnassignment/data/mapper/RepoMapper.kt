package com.rl.abnassignment.data.mapper

import com.rl.abnassignment.data.client.model.RepositoryDTO
import com.rl.abnassignment.data.database.model.Repository
import com.rl.abnassignment.domain.model.RepositoryModel

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

fun Repository.toDomainModel(): RepositoryModel {
    return RepositoryModel(
        id = this.id,
        name = this.name,
        fullName = this.fullName,
        description = this.description,
        htmlUrl = this.htmlUrl,
        visibility = this.visibility,
        isPrivate = this.isPrivate,
        ownerAvatarUrl = this.ownerAvatarUrl
    )
}

