package com.nuun.track.domain.reservation.usecase

import com.nuun.track.core.configs.base.BaseSuspendUseCase
import com.nuun.track.core.configs.state.ResultState
import com.nuun.track.domain.reservation.ReservationRepository
import com.nuun.track.domain.reservation.request.UpdateReservationRequest
import com.nuun.track.domain.reservation.response.ReservationDomain
import javax.inject.Inject

class UpdateReservationUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository
) : BaseSuspendUseCase<UpdateReservationUseCase.RequestValues, UpdateReservationUseCase.ResponseValues>() {

    class RequestValues(val request: UpdateReservationRequest) : BaseSuspendUseCase.RequestValues
    class ResponseValues(val result: ResultState<ReservationDomain?>) :
        BaseSuspendUseCase.ResponseValues

    override suspend fun execute(requestValues: RequestValues): ResponseValues {
        return try {
            val result = reservationRepository.postUpdateReservation(requestValues.request)
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