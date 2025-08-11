package com.nuun.track.core.configs.networking

import com.nuun.track.core.configs.base.dto.BaseResponseWrapper
import com.nuun.track.utility.logger.ApiLogger
import retrofit2.Response

object ApiHandler {
    val apiLogger = ApiLogger()
    suspend fun <T : Any> handleApi(
        errorHandler: ErrorResponseHandler = DefaultErrorResponseHandler(),
        block: suspend () -> Response<T>
    ): T? {
        try {
            apiLogger.debug("Executing API call..")
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
            apiLogger.debug("Exception in api Call $e")
            throw e
        }
    }
}