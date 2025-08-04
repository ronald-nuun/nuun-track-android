package com.nuun.track.data.reservation.dto

import com.nuun.track.domain.reservation.response.PricingDomain
import com.nuun.track.utility.extensions.orZero
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PricingDto(
    @Json(name = "price_per_hour")
    val pricePerHour: Double? = null,
    @Json(name = "total_price")
    val totalPrice: Double? = null,
    @Json(name = "currency")
    val currency: String? = null
) {
    fun toDomain(): PricingDomain = PricingDomain(
        pricePerHour = pricePerHour.orZero(),
        totalPrice = totalPrice.orZero(),
        currency = currency.orEmpty()
    )
}
