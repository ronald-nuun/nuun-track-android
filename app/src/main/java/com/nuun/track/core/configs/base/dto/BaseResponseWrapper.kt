package com.nuun.track.core.configs.base.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponseWrapper<T: Any>(
    @Json(name = "success")
    var success: Boolean = false,
    @Json(name = "message")
    var message: String? = null,
    @Json(name = "data")
    var data: T? = null
)