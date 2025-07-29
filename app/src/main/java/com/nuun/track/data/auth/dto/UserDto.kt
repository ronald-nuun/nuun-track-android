package com.nuun.track.data.auth.dto

import com.nuun.track.domain.auth.response.UserDomain
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "user")
    val user: UserDetailDto? = null,
    @Json(name = "token")
    val token: String? = null,
) {

    fun toDomain(): UserDomain = UserDomain(
        user = user?.toDomain(),
        token = token,
    )

    @JsonClass(generateAdapter = true)
    data class UserDetailDto(
        @Json(name = "id")
        val id: Int? = null,
        @Json(name = "name")
        val name: String? = null,
        @Json(name = "email")
        val email: String? = null,
    ) {
        fun toDomain(): UserDomain.UserDetailDomain = UserDomain.UserDetailDomain(
            id = id,
            name = name,
            email = email,
        )
    }
}
