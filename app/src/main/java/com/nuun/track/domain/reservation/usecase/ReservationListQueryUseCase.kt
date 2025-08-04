package com.nuun.track.domain.reservation.usecase

import com.nuun.track.core.configs.base.BaseSuspendUseCase
import com.nuun.track.core.configs.state.ResultState
import com.nuun.track.domain.reservation.ReservationRepository
import com.nuun.track.domain.reservation.request.ReservationRequest
import com.nuun.track.domain.reservation.response.ReservationDomain
import javax.inject.Inject

data class ReservationListQueryUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository
) : BaseSuspendUseCase<ReservationListQueryUseCase.RequestValues, ReservationListQueryUseCase.ResponseValues>() {

    class RequestValues(val request: ReservationRequest) : BaseSuspendUseCase.RequestValues
    class ResponseValues(val result: ResultState<List<ReservationDomain>?>) :
        BaseSuspendUseCase.ResponseValues

    override suspend fun execute(requestValues: RequestValues): ResponseValues {
        return try {
            val result = reservationRepository.postReservationListQuery(requestValues.request)
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
