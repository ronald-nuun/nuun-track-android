package com.nuun.track.utility.extensions

import java.text.NumberFormat
import java.util.Locale

fun Double?.orZero(): Double = this ?: 0.0

fun Double?.formatToCurrency(): String {
    if (this == null) return "0"

    val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
    formatter.maximumFractionDigits = 0 // no decimal places
    return formatter.format(this)
}