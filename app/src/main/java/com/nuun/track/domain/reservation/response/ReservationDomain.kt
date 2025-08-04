package com.nuun.track.domain.reservation.response

data class ReservationDomain(
    val id: Int,
    val customer: CustomerDomain? = null,
    val vehicle: VehicleDomain? = null,
    val reservationDetail: ReservationDetailDomain? = null,
    val pricing: PricingDomain? = null,
    val timestamps: TimestampsDomain? = null,
    val notes: String? = null
)
