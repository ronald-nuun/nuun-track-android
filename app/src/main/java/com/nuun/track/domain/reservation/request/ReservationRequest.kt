package com.nuun.track.domain.reservation.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReservationRequest(
    @Json(name = "query")
    var query: String = "",
)
