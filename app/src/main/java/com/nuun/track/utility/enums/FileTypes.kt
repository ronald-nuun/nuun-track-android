package com.nuun.track.utility.enums

import com.squareup.moshi.Json

enum class FileTypes {
    @Json(name = "photo")
    PHOTO,
    @Json(name = "video")
    VIDEO,
}