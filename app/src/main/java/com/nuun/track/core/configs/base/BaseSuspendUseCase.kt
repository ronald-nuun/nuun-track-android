package com.nuun.track.core.configs.base

abstract class BaseSuspendUseCase<REQUEST : BaseSuspendUseCase.RequestValues, RESPONSE : BaseSuspendUseCase.ResponseValues> {

    abstract suspend fun execute(requestValues: REQUEST): RESPONSE

    interface RequestValues

    interface ResponseValues

}