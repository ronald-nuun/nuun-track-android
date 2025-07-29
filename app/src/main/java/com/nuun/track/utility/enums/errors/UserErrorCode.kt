package com.nuun.track.utility.enums.errors

enum class UserErrorCode(
    val code: Int,
    val message: String
) {
    INCORRECT_PASSWORD(
        10001,
        "Incorrect login password"
    ),
    INCORRECT_VERIFICATION_CODE(
        10002,
        "Incorrect verification code"
    ),
    USER_NOT_EXIST(
        10003,
        "User does not exist."
    ),
    USER_NOT_IN_REGION(
        10004,
        "The user to log in is not in current region."
    ),
    EXISTING_USER(
        10009,
        "You are creating an account for an existing user email/phone number."
    ),
    VERIFICATION_CODE_TOO_FAST(
        10010,
        "You are sending verification code too fast."
    );

    companion object {
        fun fromCode(code: Int): UserErrorCode? =
            entries.find { it.code == code }
    }
}