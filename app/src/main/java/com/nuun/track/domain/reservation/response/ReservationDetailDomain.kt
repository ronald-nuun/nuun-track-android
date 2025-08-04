package com.nuun.track.domain.reservation.response

import com.nuun.track.utility.enums.ReservationStatus

data class ReservationDetailDomain(
    val startDateTime: String?,
    val endDateTime: String?,
    val durationHours: Int?,
    val status: ReservationStatus?,
    val pickupLocation: String?,
    val pickupAddress: String?,
    val pickupCoordinates: CoordinatesDomain?,
    val returnLocation: String?,
    val returnAddress: String?,
    val returnCoordinates: CoordinatesDomain?
)
