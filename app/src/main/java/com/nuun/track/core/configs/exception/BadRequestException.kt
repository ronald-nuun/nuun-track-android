package com.nuun.track.core.configs.exception

/**
 * BadRequestException is thrown when api response code is 400
 * This is handleable manually
 */
data class BadRequestException(
    override val message: String,
    val errorFlag: String,
    val code: Int,
) : Exception(message)