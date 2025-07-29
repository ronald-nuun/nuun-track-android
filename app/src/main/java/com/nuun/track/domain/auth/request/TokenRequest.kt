package com.nuun.track.domain.auth.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenRequest(
    @Json(name = "rt")
    val refreshToken: String? = null,
)