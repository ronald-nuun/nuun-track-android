package com.nuun.track.core.configs.networking

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DefaultErrorResponse(
    @Json(name = "code")
    val code: Int? = null,
    @Json(name = "message")
    val message: String? = null,
    @Json(name = "flag")
    val flag: String? = null
)