package com.rl.abnassignment.data.client.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OwnerDTO(
    val avatar_url: String
)