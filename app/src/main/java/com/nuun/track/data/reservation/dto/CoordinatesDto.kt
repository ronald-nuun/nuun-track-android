package com.nuun.track.data.reservation.dto

import com.nuun.track.domain.reservation.response.CoordinatesDomain
import com.nuun.track.utility.extensions.orZero
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoordinatesDto(
    @Json(name = "latitude")
    val latitude: Double? = null,
    @Json(name = "longitude")
    val longitude: Double? = null
) {
    fun toDomain(): CoordinatesDomain = CoordinatesDomain(
        latitude = latitude.orZero(),
        longitude = longitude.orZero()
    )
}
