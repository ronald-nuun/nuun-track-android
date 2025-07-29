package com.nuun.track.core.configs.networking

import com.nuun.track.core.configs.exception.BadRequestException
import okhttp3.ResponseBody

class DefaultErrorResponseHandler : ErrorResponseHandler() {

    override fun handleBadRequestError(responseBody: ResponseBody): BadRequestException {
        val errorResponseType = DefaultErrorResponse::class.java
        val errorWrapper = responseBody.errorBodyParser<DefaultErrorResponse>(errorResponseType)
        return BadRequestException(
            code = errorWrapper?.code ?: 0,
            message = errorWrapper?.message.orEmpty(),
            errorFlag = errorWrapper?.flag.orEmpty()
        )
    }

    override fun handleRespBadRequestError(message: String?, errorCode: Int): BadRequestException {
        return BadRequestException(
            code = errorCode,
            message = message.orEmpty(),
            errorFlag = errorCode.toString()
        )
    }

}