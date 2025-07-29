package com.nuun.track.domain.auth.response

data class UserDomain(
    val user: UserDetailDomain?,
    val token: String?,
) {
    data class UserDetailDomain(
        val id: Int?,
        val name: String?,
        val email: String?,
    )
}