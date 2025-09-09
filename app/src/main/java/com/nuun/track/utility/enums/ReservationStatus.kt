package com.nuun.track.utility.enums

import com.squareup.moshi.Json

enum class ReservationStatus(val code: Int) {
    @Json(name = "pending")
    PENDING(0),
    @Json(name = "start")
    START(1),
    @Json(name = "ongoing")
    ONGOING(2),
    @Json(name = "end")
    END(3)
}