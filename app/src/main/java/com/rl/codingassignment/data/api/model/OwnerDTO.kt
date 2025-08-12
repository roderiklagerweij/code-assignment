package com.rl.codingassignment.data.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OwnerDTO(
    val avatar_url: String
)