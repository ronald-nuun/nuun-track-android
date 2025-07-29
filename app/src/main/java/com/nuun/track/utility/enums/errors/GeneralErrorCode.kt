package com.nuun.track.utility.enums.errors

enum class GeneralErrorCode(
    val code: Int,
    val message: String
) {
    PARAMETER_ERROR(
        400,
        "Parameter error, usually caused by missing or incorrect parameters."
    ),
    AUTHENTICATION_ERROR(
        401,
        "Authentication error with access token."
    ),
    TOKEN_EXPIRED(
        402,
        "Access token expired."
    ),
    INTERFACE_NOT_FOUND(
        403,
        "Cannot find the interface, usually because the URL is wrong."
    ),
    RESOURCE_NOT_FOUND(
        405,
        "The resource cannot be found in the back-end database."
    ),
    ACCESS_DENIED(
        406,
        "Access denied. You do not have permission."
    ),
    APPID_NO_PERMISSION(
        407,
        "The appid has no permission for this action."
    ),
    APPID_LIMIT_EXCEEDED(
        412,
        "APPID calls exceed the limit."
    ),
    SERVER_ERROR(
        500,
        "Internal server error."
    ),
    DEVICE_CONTROL_FAILURE(
        4002,
        "Device control failure. Check control parameter transmission or device online status."
    );

    companion object {
        fun fromCode(code: Int): GeneralErrorCode? =
            entries.find { it.code == code }
    }
}