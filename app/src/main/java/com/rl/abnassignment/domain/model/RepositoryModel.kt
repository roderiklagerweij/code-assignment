package com.rl.abnassignment.domain.model

data class RepositoryModel(
    val id: Int,
    val name: String,
    val fullName: String,
    val description: String?,
    val htmlUrl: String,
    val visibility: String,
    val isPrivate: Boolean,
    val ownerAvatarUrl: String
)