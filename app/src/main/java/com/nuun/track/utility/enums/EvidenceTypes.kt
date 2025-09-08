package com.nuun.track.utility.enums

import androidx.annotation.DrawableRes
import com.nuun.track.R

enum class EvidenceTypes(
    val label: String,
    @param:DrawableRes val exampleImage: Int
) {
    EXTERIOR_DEPAN("Exterior Depan", R.drawable.ic_car_front),
    EXTERIOR_BELAKANG("Exterior Belakang", R.drawable.ic_car_back),
    EXTERIOR_KANAN("Exterior Kanan", R.drawable.ic_car_right),
    EXTERIOR_KIRI("Exterior Kiri", R.drawable.ic_car_left),
    INTERIOR_DEPAN("Interior Depan", R.drawable.ic_car_ext_front),
    INTERIOR_BELAKANG("Interior Belakang", R.drawable.ic_car_ext_back);
}