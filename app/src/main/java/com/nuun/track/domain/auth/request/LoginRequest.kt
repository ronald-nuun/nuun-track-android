package com.nuun.track.domain.auth.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "email")
    val email: String? = null,
    @Json(name = "password")
    val password: String? = null,
)