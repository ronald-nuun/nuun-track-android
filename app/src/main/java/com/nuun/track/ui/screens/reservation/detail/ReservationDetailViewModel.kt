package com.nuun.track.ui.screens.reservation.detail

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuun.track.core.configs.state.ResultState
import com.nuun.track.domain.reservation.request.ReservationDetailRequest
import com.nuun.track.domain.reservation.request.UpdateReservationRequest
import com.nuun.track.domain.reservation.response.ReservationDomain
import com.nuun.track.domain.reservation.usecase.ReservationDetailUseCase
import com.nuun.track.domain.reservation.usecase.UpdateReservationUseCase
import com.nuun.track.utility.extensions.uriToMultipart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class ReservationDetailViewModel @Inject constructor(
    private val updateReservationUseCase: UpdateReservationUseCase,
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

    private val _selectedUris = MutableStateFlow<List<Uri>>(emptyList())
    val selectedUris: StateFlow<List<Uri>> = _selectedUris.asStateFlow()

    fun addUri(uri: Uri) {
        if (uri !in _selectedUris.value) {
            _selectedUris.value = _selectedUris.value + uri
        }
    }

    fun removeUri(uri: Uri) {
        _selectedUris.value = _selectedUris.value - uri
    }

    fun clearUri() {
        _selectedUris.value = emptyList()
    }

    private val _hasFetchedReservationDetail by lazy { MutableStateFlow(false) }
    val hasFetchedReservationDetail: StateFlow<Boolean> get() = _hasFetchedReservationDetail.asStateFlow()

    fun setHasFetchedReservationDetail(hasFetched: Boolean) {
        _hasFetchedReservationDetail.value = hasFetched
    }

    private val _isLoading by lazy { MutableStateFlow(false) }
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _resultUpdateReservation by lazy {
        MutableStateFlow<ResultState<ReservationDomain?>>(
            ResultState.Success(null)
        )
    }
    val resultUpdateReservation: StateFlow<ResultState<ReservationDomain?>> get() = _resultUpdateReservation.asStateFlow()

    fun updateReservation(
        context: Context,
        reservationId: Int,
        customerId: Int,
        vehicleId: Int,
        files: List<Uri>
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val evidences = files.mapNotNull { uri ->
                context.uriToMultipart(uri)
            }

            val request = UpdateReservationRequest(
                reservationId = reservationId.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull()),
                customerId = customerId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                vehicleId = vehicleId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                files = evidences
            )

            updateReservationUseCase.execute(UpdateReservationUseCase.RequestValues(request)).result.let {
                _isLoading.value = false
                _resultUpdateReservation.emit(it)
            }
        }
    }

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