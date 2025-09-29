package com.nuun.track.ui.screens.reservation.detail

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nuun.track.R
import com.nuun.track.core.configs.state.ResultState
import com.nuun.track.domain.configs.TextIconConfig
import com.nuun.track.domain.configs.ToolbarConfig
import com.nuun.track.domain.reservation.request.ReservationDetailRequest
import com.nuun.track.domain.reservation.response.ReservationDomain
import com.nuun.track.navigation.screens.HomeNavScreen
import com.nuun.track.ui.common.HandleErrorStates
import com.nuun.track.ui.components.button.DashedBorderImageVectorButton
import com.nuun.track.ui.components.cards.DefaultCard
import com.nuun.track.ui.components.media.MediaPreview
import com.nuun.track.ui.components.media.MediaPreviewDialog
import com.nuun.track.ui.components.texts.TextIcon
import com.nuun.track.ui.components.toolbar.CustomToolbar
import com.nuun.track.ui.shared_viewmodel.MoshiViewModel
import com.nuun.track.ui.theme.ColorBgIcon
import com.nuun.track.ui.theme.ColorContainer
import com.nuun.track.ui.theme.ColorTextDefault
import com.nuun.track.utility.consts.AppConsts
import com.nuun.track.utility.consts.ExtraConst
import com.nuun.track.utility.enums.EvidenceTypes
import com.nuun.track.utility.enums.FileTypes
import com.nuun.track.utility.enums.ReservationStatus
import com.nuun.track.utility.extensions.formatToCurrency
import com.nuun.track.utility.extensions.toastShortExt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDetailScreen(
    navController: NavController,
    moshiViewModel: MoshiViewModel = hiltViewModel(),
    reservationDetailViewModel: ReservationDetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val reservation by reservationDetailViewModel.reservation.collectAsState()
    val isLoading by reservationDetailViewModel.isLoading.collectAsState()
    val isRefreshing by reservationDetailViewModel.isRefreshing.collectAsState()
    val status = reservation?.reservationDetail?.status
    val resultReservationDetail by reservationDetailViewModel.resultReservationDetail.collectAsState()

    val videoToPlay by reservationDetailViewModel.videoToPlay.collectAsState()
    val imageToPreview by reservationDetailViewModel.imageToPreview.collectAsState()
    val hasFetchedReservationDetail by reservationDetailViewModel.hasFetchedReservationDetail.collectAsState()

    LaunchedEffect(Unit) {
        val jsonString =
            navController.currentBackStackEntry?.arguments?.getString(ExtraConst.EXTRA_RESERVATION)
        val reserve = jsonString?.let {
            moshiViewModel.fromJson(jsonString, ReservationDomain::class.java)
        }
        reservationDetailViewModel.setReservation(reserve)
        val refresh =
            navController.currentBackStackEntry?.arguments?.getBoolean(ExtraConst.EXTRA_REFRESH)
                ?: false
        if (refresh) {
            reservationDetailViewModel.getReservationDetail(
                ReservationDetailRequest(
                    reservationId = reserve?.id.toString()
                )
            )
        }
    }

    val refreshState = rememberPullToRefreshState()
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            reservationDetailViewModel.getReservationDetail(
                ReservationDetailRequest(
                    reservationId = reservation?.id.toString()
                )
            )
            reservationDetailViewModel.updateIsRefreshing(false)
        }
    }

    when (val result = resultReservationDetail) {
        is ResultState.Success -> {
            if (!hasFetchedReservationDetail && result.data != null) {
                reservationDetailViewModel.setHasFetchedReservationDetail(true)
                reservationDetailViewModel.setReservation(result.data)
            }
        }

        is ResultState.Error -> {
            HandleErrorStates(
                navController = navController,
                states = arrayOf(
                    result,
                )
            )
        }
    }

    Scaffold(
        topBar = {
            InitToolBar(
                context = context,
                navController = navController,
                isLoading = isLoading
            )
        },
        containerColor = ColorContainer,
    ) { paddingValues ->
        PullToRefreshBox(
            state = refreshState,
            onRefresh = { reservationDetailViewModel.updateIsRefreshing(true) },
            isRefreshing = isRefreshing,
        ) {
            Column(
                Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(10.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                CustomerDetail(reservation)
                Spacer(modifier = Modifier.height(10.dp))
                ReservationDetail(reservation)
                reservation?.let {
                    Evidences(
                        navController,
                        reservationDetailViewModel,
                        it,
                        status,
                    )
                }

                if (!isLoading) {
                    videoToPlay?.let { (title, uris) ->
                        MediaPreviewDialog(
                            title = title,
                            uris = uris,
                            isVideo = true,
                            onDismiss = { reservationDetailViewModel.setVideoToPlay(null) },
                        )
                    }

                    imageToPreview?.let { (title, uris) ->
                        MediaPreviewDialog(
                            title = title,
                            uris = uris,
                            isVideo = false,
                            onDismiss = { reservationDetailViewModel.setImageToPreview(null) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InitToolBar(
    context: Context,
    navController: NavController,
    isLoading: Boolean,
) {
    CustomToolbar(
        config = ToolbarConfig(
            showTitle = true,
            title = stringResource(R.string.label_reservation_detail),
            showLogo = false,
        ),
        onBackClick = {
            if (!isLoading) {
                navController.popBackStack()
            } else {
                context.toastShortExt(context.getString(R.string.label_wait_evidence_upload))
            }
        },
    )
}

@Composable
fun CustomerDetail(reservation: ReservationDomain?) {
    DefaultCard(label = stringResource(R.string.label_customer_information)) {
        TextIcon(
            TextIconConfig(
                title = stringResource(R.string.label_name),
                message = reservation?.customer?.name,
                useIndentation = false,
            )
        )
        TextIcon(
            TextIconConfig(
                title = stringResource(R.string.label_email),
                message = reservation?.customer?.email,
                useIndentation = false,
            )
        )
        TextIcon(
            TextIconConfig(
                title = stringResource(R.string.label_phone_number),
                message = reservation?.customer?.phone,
                useIndentation = false,
                fullDivider = false
            )
        )
    }
}

@Composable
fun ReservationDetail(reservation: ReservationDomain?) {
    DefaultCard(label = stringResource(R.string.label_reservation_detail)) {
        TextIcon(
            TextIconConfig(
                title = stringResource(R.string.label_reservation_date),
                message = reservation?.reservationDetail?.startDateTime,
                useIndentation = false,
            )
        )
        TextIcon(
            TextIconConfig(
                title = stringResource(R.string.label_reservation_date_return),
                message = reservation?.reservationDetail?.endDateTime,
                useIndentation = false,
            )
        )
        TextIcon(
            TextIconConfig(
                title = stringResource(R.string.label_duration),
                message = "${reservation?.reservationDetail?.durationHours} jam",
                useIndentation = false,
                fullDivider = false
            )
        )
        TextIcon(
            TextIconConfig(
                title = stringResource(R.string.label_total_price),
                message = "${reservation?.pricing?.currency} ${reservation?.pricing?.totalPrice?.formatToCurrency()}",
                useIndentation = false,
                fullDivider = false,
            )
        )
    }
}

@Composable
fun Evidences(
    navController: NavController,
    reservationDetailViewModel: ReservationDetailViewModel,
    reservation: ReservationDomain,
    status: ReservationStatus?
) {
    when (status) {
        ReservationStatus.ONGOING -> {
            DisplayEvidence(
                reservationDetailViewModel,
                reservation,
                stringResource(R.string.label_vehicle_evidence_start),
                true
            )
            AddEvidence(navController, reservationDetailViewModel)
        }

        ReservationStatus.END -> {
            DisplayEvidence(
                reservationDetailViewModel,
                reservation,
                stringResource(R.string.label_vehicle_evidence_start),
                true
            )
            DisplayEvidence(
                reservationDetailViewModel,
                reservation,
                stringResource(R.string.label_vehicle_evidence_end),
                false
            )
        }

        else -> {
            AddEvidence(navController, reservationDetailViewModel)
        }
    }
}

@Composable
fun AddEvidence(
    navController: NavController,
    reservationDetailViewModel: ReservationDetailViewModel
) {
    val isLoading by reservationDetailViewModel.isLoading.collectAsState()
    val reservation by reservationDetailViewModel.reservation.collectAsState()

    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = stringResource(R.string.label_upload_vehicle_evidence),
        color = ColorTextDefault,
    )
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.label_evidence_tips),
        color = ColorTextDefault,
        fontSize = 11.sp
    )
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        maxItemsInEachRow = Int.MAX_VALUE,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        itemVerticalAlignment = Alignment.CenterVertically,
    ) {
        DashedBorderImageVectorButton(
            imageVector = Icons.Default.Add,
            contentDescription = "Add media",
            onClick = {
                if (!isLoading)
                    reservation?.let {
                        navController.navigate(HomeNavScreen.ReservationForm.createRoute(it))
                    }
            }
        )
    }
}

@Composable
fun DisplayEvidence(
    reservationDetailViewModel: ReservationDetailViewModel,
    reservation: ReservationDomain,
    title: String,
    evidenceStart: Boolean,
) {
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = title,
        color = ColorTextDefault
    )
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.label_evidence_tips),
        color = ColorTextDefault,
        fontSize = 11.sp
    )
    Spacer(modifier = Modifier.height(10.dp))

    val labels = EvidenceTypes.entries.map { it.label }
    val evidences = if (evidenceStart) reservation.evidenceStart else reservation.evidenceEnd

    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // Fix 3 columns per row
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 0.dp, max = 430.dp)
    ) {
        itemsIndexed(evidences ?: emptyList()) { index, evidence ->
            val urlUri: Uri = (AppConsts.API_URL + evidence.fileUrl).toUri()
            val uris = buildList {
                reservation.evidenceStart?.getOrNull(index)?.let { evidence ->
                    add((AppConsts.API_URL + evidence.fileUrl).toUri())
                }
                reservation.evidenceEnd?.getOrNull(index)?.let { evidence ->
                    add((AppConsts.API_URL + evidence.fileUrl).toUri())
                }
            }
            val preview = labels[index] to uris

            Column(
                modifier = Modifier.clickable {
                    when (evidence.fileType) {
                    FileTypes.PHOTO -> {
                        reservationDetailViewModel.setVideoToPlay(null)
                        reservationDetailViewModel.setImageToPreview(preview)
                    }

                    else -> {
                        reservationDetailViewModel.setImageToPreview(null)
                        reservationDetailViewModel.setVideoToPlay(preview)
                    }
                }
            }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                topStart = 10.dp,
                                topEnd = 10.dp,
                                bottomEnd = 0.dp,
                                bottomStart = 0.dp
                            )
                        )
                        .background(ColorBgIcon)
                ) {
                    MediaPreview(uri = urlUri, type = evidence.fileType)
                }

                labels.getOrNull(index)?.let {
                    SetupMediaCaption(it, 10.dp)
                }
            }
        }
    }

}

@Composable
fun SetupMediaCaption(
    label: String,
    cornerRadius: Dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomEnd = cornerRadius,
                    bottomStart = cornerRadius
                )
            )
            .background(Color.White.copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.Black,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}