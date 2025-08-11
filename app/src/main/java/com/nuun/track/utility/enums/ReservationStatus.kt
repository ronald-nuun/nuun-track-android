package com.nuun.track.utility.enums

import com.squareup.moshi.Json

enum class ReservationStatus {
    @Json(name = "pending")
    PENDING,
    @Json(name = "start")
    START,
    @Json(name = "ongoing")
    ONGOING,
    @Json(name = "end")
    END
}