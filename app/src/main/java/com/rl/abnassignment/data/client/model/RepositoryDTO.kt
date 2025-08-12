package com.rl.abnassignment.data.client.model

import com.squareup.moshi.JsonClass

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
)
