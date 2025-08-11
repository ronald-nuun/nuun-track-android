package com.nuun.track.ui.screens.reservation.detail

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nuun.track.R
import com.nuun.track.core.configs.state.ResultState
import com.nuun.track.domain.configs.TextIconConfig
import com.nuun.track.domain.configs.ToolbarConfig
import com.nuun.track.domain.reservation.request.ReservationDetailRequest
import com.nuun.track.domain.reservation.response.ReservationDomain
import com.nuun.track.ui.common.HandleErrorStates
import com.nuun.track.ui.components.button.CustomButton
import com.nuun.track.ui.components.cards.DefaultCard
import com.nuun.track.ui.components.image.ImageWithLoading
import com.nuun.track.ui.components.texts.TextIcon
import com.nuun.track.ui.components.toolbar.CustomToolbar
import com.nuun.track.ui.shared_viewmodel.MoshiViewModel
import com.nuun.track.ui.theme.ColorBgIcon
import com.nuun.track.ui.theme.ColorContainer
import com.nuun.track.ui.theme.ColorTextDefault
import com.nuun.track.utility.consts.AppConsts
import com.nuun.track.utility.consts.ExtraConst
import com.nuun.track.utility.enums.FileTypes
import com.nuun.track.utility.enums.ReservationStatus
import com.nuun.track.utility.extensions.formatToCurrency
import com.nuun.track.utility.extensions.getMimeType
import com.nuun.track.utility.extensions.getVideoThumbnailFromUri
import com.nuun.track.utility.extensions.orZero
import com.nuun.track.utility.extensions.toastShortExt

@Composable
fun ReservationDetailScreen(
    navController: NavController,
    moshiViewModel: MoshiViewModel = hiltViewModel(),
    reservationDetailViewModel: ReservationDetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val reservation by reservationDetailViewModel.reservation.collectAsState()
    val isLoading by reservationDetailViewModel.isLoading.collectAsState()
    val status = reservation?.reservationDetail?.status
    val resultUpdateReservation by reservationDetailViewModel.resultUpdateReservation.collectAsState()
    val resultReservationDetail by reservationDetailViewModel.resultReservationDetail.collectAsState()

    val selectedUris by reservationDetailViewModel.selectedUris.collectAsState()
    val videoToPlay by reservationDetailViewModel.videoToPlay.collectAsState()
    val imageToPreview by reservationDetailViewModel.imageToPreview.collectAsState()
    val hasFetchedReservationDetail by reservationDetailViewModel.hasFetchedReservationDetail.collectAsState()

    LaunchedEffect(Unit) {
        val jsonString =
            navController.currentBackStackEntry?.arguments?.getString(ExtraConst.EXTRA_RESERVATION)
        reservationDetailViewModel.setReservation(
            jsonString?.let { moshiViewModel.fromJson(jsonString, ReservationDomain::class.java) }
        )
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

    LaunchedEffect(resultUpdateReservation) {
        reservationDetailViewModel.clearUri()
        if (resultUpdateReservation is ResultState.Success) {
            val data = (resultUpdateReservation as ResultState.Success<ReservationDomain?>).data
            if (data != null && reservation != null) {
                reservationDetailViewModel.getReservationDetail(
                    ReservationDetailRequest(
                        reservationId = reservation?.id.toString()
                    )
                )
                reservationDetailViewModel.setHasFetchedReservationDetail(false)
            }
        }
    }

    if (resultUpdateReservation is ResultState.Error) {
        HandleErrorStates(
            navController = navController,
            states = arrayOf(resultUpdateReservation),
        )
    }

    Scaffold(
        topBar = {
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
        },
        containerColor = ColorContainer,
    ) { paddingValues ->
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
                    reservationDetailViewModel,
                    it,
                    status,
                )
            }

            if (!isLoading) {
                videoToPlay?.let {
                    MediaPreviewDialog(
                        uri = it,
                        isVideo = true,
                        onDismiss = { reservationDetailViewModel.setVideoToPlay(null) }
                    )
                }

                imageToPreview?.let {
                    MediaPreviewDialog(
                        uri = it,
                        isVideo = false,
                        onDismiss = { reservationDetailViewModel.setImageToPreview(null) }
                    )
                }
            }

            if (status == ReservationStatus.START || status == ReservationStatus.ONGOING) {
                Spacer(Modifier.weight(1f))
                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (status == ReservationStatus.START) {
                        stringResource(R.string.label_start_trip)
                    } else {
                        stringResource(R.string.label_return_confirmation)
                    },
                    isLoadingEnabled = isLoading,
                    onClick = {
                        if (selectedUris.isNotEmpty()) {
                            reservationDetailViewModel.setHasFetchedReservationDetail(false)
                            reservationDetailViewModel.updateReservation(
                                context = context,
                                reservationId = reservation?.id.orZero(),
                                customerId = reservation?.customer?.id.orZero(),
                                vehicleId = reservation?.vehicle?.id.orZero(),
                                files = selectedUris
                            )
                        } else {
                            context.toastShortExt(context.getString(R.string.label_upload_vehicle_evidence))
                        }
                    },
                )
            }
        }
    }
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
            AddEvidence(reservationDetailViewModel)
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
            AddEvidence(reservationDetailViewModel)
        }
    }
}

@Composable
fun AddEvidence(
    reservationDetailViewModel: ReservationDetailViewModel
) {
    val context = LocalContext.current
    val isLoading by reservationDetailViewModel.isLoading.collectAsState()
    val selectedUris by reservationDetailViewModel.selectedUris.collectAsState()
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        if (!uris.isNullOrEmpty()) {
            uris.forEach { uri ->
                val isAlreadySelected = selectedUris.any { it.toString() == uri.toString() }
                if (!isAlreadySelected) {
                    reservationDetailViewModel.addUri(uri)
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = stringResource(R.string.label_upload_vehicle_evidence),
        color = ColorTextDefault,
    )
    FlowRow(
        maxItemsInEachRow = Int.MAX_VALUE,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        selectedUris.forEach { uri ->
            Box(modifier = Modifier.clickable {
                if (!isLoading) {
                    val mimeType = context.contentResolver.getType(uri)
                    when {
                        mimeType?.startsWith("video") == true -> {
                            reservationDetailViewModel.setImageToPreview(null)
                            reservationDetailViewModel.setVideoToPlay(uri)
                        }

                        mimeType?.startsWith("image") == true -> {
                            reservationDetailViewModel.setVideoToPlay(null)
                            reservationDetailViewModel.setImageToPreview(uri)
                        }
                    }
                }
            }) {
                MediaPreviewWithRemove(
                    uri = uri,
                    onRemove = {
                        if (!isLoading) reservationDetailViewModel.removeUri(it)
                    }
                )
            }
        }
        DashedBorderAddButton(onClick = {
            if (!isLoading)
                mediaPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                )
        })
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
    Spacer(modifier = Modifier.height(10.dp))

    val evidences = if (evidenceStart) reservation.evidenceStart else reservation.evidenceEnd
    FlowRow(
        maxItemsInEachRow = Int.MAX_VALUE,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        evidences?.forEach { evidence ->
            Box(modifier = Modifier.clickable {
                when (evidence.fileType) {
                    FileTypes.PHOTO -> {
                        reservationDetailViewModel.setVideoToPlay(null)
                        reservationDetailViewModel.setImageToPreview((AppConsts.API_URL + evidence.fileUrl).toUri())
                    }

                    else -> {
                        reservationDetailViewModel.setImageToPreview(null)
                        reservationDetailViewModel.setVideoToPlay((AppConsts.API_URL + evidence.fileUrl).toUri())
                    }
                }
            }) {
                evidence.fileType?.let { type ->
                    if (!evidence.fileUrl.isNullOrEmpty()) {
                        MediaPreview(
                            uri = (AppConsts.API_URL + evidence.fileUrl).toUri(),
                            type = type
                        )
                    } else {
                        evidence.url?.let { url ->
                            MediaPreview(
                                uri = url.toUri(),
                                type = type
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashedBorderAddButton(onClick: () -> Unit) {
    val strokeWidth = 2.dp
    val dashWidthDp = 9.dp
    val dashGapDp = 9.dp
    val cornerRadius = 12.dp

    val density = LocalDensity.current
    val dashWidthPx = with(density) { dashWidthDp.toPx() }
    val dashGapPx = with(density) { dashGapDp.toPx() }
    val strokeWidthPx = with(density) { strokeWidth.toPx() }
    val cornerRadiusPx = with(density) { cornerRadius.toPx() }

    Surface(
        onClick = onClick,
        color = Color.Transparent,
        shape = RoundedCornerShape(cornerRadius),
        modifier = Modifier
            .size(90.dp)
            .padding(2.dp)
            .drawBehind {
                val stroke = Stroke(
                    width = strokeWidthPx,
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(dashWidthPx, dashGapPx),
                        0f
                    )
                )

                val rect = size.toRect().deflate(stroke.width / 2)
                val path = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = rect,
                            cornerRadius = CornerRadius(cornerRadiusPx)
                        )
                    )
                }

                drawPath(
                    path = path,
                    color = Color.Gray,
                    style = stroke
                )
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add media",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun MediaPreviewWithRemove(
    modifier: Modifier = Modifier.size(90.dp),
    uri: Uri,
    onRemove: (Uri) -> Unit,
) {
    Box(modifier = modifier) {
        MediaPreview(
            modifier = Modifier.matchParentSize(),
            uri
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(20.dp)
                .background(Color.Red.copy(alpha = 0.7f), shape = CircleShape)
                .clickable { onRemove(uri) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.desc_remove),
                tint = Color.White,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Composable
fun MediaPreview(
    modifier: Modifier = Modifier.size(90.dp),
    uri: Uri, type: FileTypes? = null
) {
    val context = LocalContext.current
    val mimeType = remember(uri) { context.getMimeType(uri) }
    val cornerRadius = 10.dp

    when {
        type == FileTypes.PHOTO || mimeType?.startsWith("image") == true -> {
            ImageWithLoading(
                uri = uri,
                cornerRadius = cornerRadius,
            )
        }

        type == FileTypes.VIDEO -> {
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.label_video), fontSize = 10.sp)
            }
        }

        mimeType?.startsWith("video") == true -> {
            val thumbnail = remember(uri) { context.getVideoThumbnailFromUri(uri) }
            if (thumbnail != null) {
                ImageWithLoading(
                    uri = uri,
                    cornerRadius = cornerRadius,
                )
            } else {
                Box(
                    modifier = modifier
                        .clip(RoundedCornerShape(cornerRadius))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.label_no_thumbnail), fontSize = 10.sp)
                }
            }
        }

        else -> {
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.label_unsupported_file), fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun MediaPreviewDialog(
    uri: Uri,
    isVideo: Boolean,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = ColorBgIcon
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                if (isVideo) {
                    VideoPlayer(uri = uri, modifier = Modifier.fillMaxSize())
                } else {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.Red.copy(alpha = 0.6f), shape = CircleShape)
                        .size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.desc_close),
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun VideoPlayer(uri: Uri, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val exoPlayer = remember(uri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(
        AndroidView(factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = true
            }
        }, modifier = modifier)
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}