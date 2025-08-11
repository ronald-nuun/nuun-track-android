package com.nuun.track.domain.reservation.usecase

import com.nuun.track.core.configs.base.BaseSuspendUseCase
import com.nuun.track.core.configs.state.ResultState
import com.nuun.track.domain.reservation.ReservationRepository
import com.nuun.track.domain.reservation.request.ReservationDetailRequest
import com.nuun.track.domain.reservation.response.ReservationDomain
import javax.inject.Inject

class ReservationDetailUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository
) : BaseSuspendUseCase<ReservationDetailUseCase.RequestValues, ReservationDetailUseCase.ResponseValues>() {
    class RequestValues(val request: ReservationDetailRequest) : BaseSuspendUseCase.RequestValues
    class ResponseValues(val result: ResultState<ReservationDomain?>) :
        BaseSuspendUseCase.ResponseValues

    override suspend fun execute(requestValues: RequestValues): ResponseValues {
        return try {
            val result = reservationRepository.postReservationDetail(requestValues.request)
            ResponseValues(
                ResultState.Success(result)
            )
        } catch (e: Exception) {
            ResponseValues(
                ResultState.Error(e)
            )
        }
    }
}