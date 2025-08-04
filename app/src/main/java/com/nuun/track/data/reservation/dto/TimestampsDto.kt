package com.nuun.track.data.reservation.dto

import com.nuun.track.domain.reservation.response.TimestampsDomain
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimestampsDto(
    @Json(name = "created_at")
    val createdAt: String? = null,
    @Json(name = "confirmed_at")
    val confirmAt: String? = null,
    @Json(name = "started_at")
    val startedAt: String? = null
) {
    fun toDomain() : TimestampsDomain = TimestampsDomain(
        createdAt = createdAt.orEmpty(),
        confirmAt = confirmAt.orEmpty(),
        startedAt = startedAt.orEmpty()
    )
}
