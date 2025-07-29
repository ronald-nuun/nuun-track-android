package com.nuun.track.core.configs.networking

import com.nuun.track.core.configs.base.dto.BaseResponseWrapper
import retrofit2.Response

object ApiHandler {
    suspend fun <T : Any> handleApi(
        errorHandler: ErrorResponseHandler = DefaultErrorResponseHandler(),
        block: suspend () -> Response<T>
    ): T? {
        try {
            val response = block.invoke()
            if (response.isSuccessful) {
                val body = response.body()
                if (body is BaseResponseWrapper<*> && !body.success) {
                    throw errorHandler.handleRespError(body.message, response.code())
                } else {
                    return body
                }
            }
            // When Error
            throw errorHandler.handle(response.errorBody(), response.code())
        } catch (e: Exception) {
            throw e
        }
    }
}