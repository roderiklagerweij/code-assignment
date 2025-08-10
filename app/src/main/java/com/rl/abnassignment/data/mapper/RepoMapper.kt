package com.rl.abnassignment.data.mapper

import com.rl.abnassignment.data.client.RepositoryDTO
import com.rl.abnassignment.data.database.model.Repository
import com.rl.abnassignment.domain.model.RepositoryModel

fun RepositoryModel.toDbModel(): Repository {
    return Repository(
        id = this.id,
        name = this.name
    )
}

fun RepositoryDTO.toDbModel(): Repository {
    return Repository(
        id = this.id,
        name = this.name
    )
}

fun Repository.toDomainModel(): RepositoryModel {
    return RepositoryModel(
        id = this.id,
        name = this.name
    )
}

