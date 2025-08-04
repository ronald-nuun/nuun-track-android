package com.nuun.track.data.reservation.dto

import com.nuun.track.domain.reservation.response.ReservationDetailDomain
import com.nuun.track.utility.enums.ReservationStatus
import com.nuun.track.utility.extensions.orZero
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReservationDetailDto(
    @Json(name = "start_datetime")
    val startDateTime: String? = null,
    @Json(name = "end_datetime")
    val endDateTime: String? = null,
    @Json(name = "duration_hours")
    val durationHours: Int? = null,
    @Json(name = "status")
    val status: ReservationStatus? = null,
    @Json(name = "pickup_location")
    val pickupLocation: String? = null,
    @Json(name = "pickup_address")
    val pickupAddress: String? = null,
    @Json(name = "pickup_coordinates")
    val pickupCoordinates: CoordinatesDto? = null,
    @Json(name = "return_location")
    val returnLocation: String? = null,
    @Json(name = "return_address")
    val returnAddress: String? = null,
    @Json(name = "return_coordinates")
    val returnpCoordinates: CoordinatesDto? = null,
) {
    fun toDomain(): ReservationDetailDomain = ReservationDetailDomain(
        startDateTime = startDateTime.orEmpty(),
        endDateTime = endDateTime.orEmpty(),
        durationHours = durationHours.orZero(),
        status = status,
        pickupLocation = pickupLocation.orEmpty(),
        pickupAddress = pickupAddress.orEmpty(),
        pickupCoordinates = pickupCoordinates?.toDomain(),
        returnLocation = returnLocation.orEmpty(),
        returnAddress = returnAddress.orEmpty(),
        returnCoordinates = returnpCoordinates?.toDomain()
    )
}
