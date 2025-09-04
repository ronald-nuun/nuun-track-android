package com.nuun.track.ui.screens.reservation.detail

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuun.track.core.configs.state.ResultState
import com.nuun.track.domain.reservation.request.ReservationDetailRequest
import com.nuun.track.domain.reservation.response.ReservationDomain
import com.nuun.track.domain.reservation.usecase.ReservationDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationDetailViewModel @Inject constructor(
    private val reservationDetailUseCase: ReservationDetailUseCase,
) : ViewModel() {

    private val _reservation = MutableStateFlow<ReservationDomain?>(null)
    val reservation: StateFlow<ReservationDomain?> = _reservation.asStateFlow()

    fun setReservation(data: ReservationDomain?) {
        _reservation.value = data
    }

    private val _videoToPlay = MutableStateFlow<Uri?>(null)
    val videoToPlay: StateFlow<Uri?> = _videoToPlay.asStateFlow()

    fun setVideoToPlay(uri: Uri?) {
        _videoToPlay.value = uri
    }

    private val _imageToPreview = MutableStateFlow<Uri?>(null)
    val imageToPreview: StateFlow<Uri?> = _imageToPreview.asStateFlow()

    fun setImageToPreview(uri: Uri?) {
        _imageToPreview.value = uri
    }

    private val _hasFetchedReservationDetail by lazy { MutableStateFlow(false) }
    val hasFetchedReservationDetail: StateFlow<Boolean> get() = _hasFetchedReservationDetail.asStateFlow()

    fun setHasFetchedReservationDetail(hasFetched: Boolean) {
        _hasFetchedReservationDetail.value = hasFetched
    }

    private val _isLoading by lazy { MutableStateFlow(false) }
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _resultReservationDetail by lazy {
        MutableStateFlow<ResultState<ReservationDomain?>> (
            ResultState.Success(null)
        )
    }

    val resultReservationDetail: StateFlow<ResultState<ReservationDomain?>> get() = _resultReservationDetail.asStateFlow()

    fun getReservationDetail(request: ReservationDetailRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            reservationDetailUseCase.execute(ReservationDetailUseCase.RequestValues(request)).result.let {
                _isLoading.value = false
                _resultReservationDetail.emit(it)
            }
        }
    }

}