package com.nuun.track.ui.screens.reservation.form

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.nuun.track.R
import com.nuun.track.core.configs.permissions.CameraPermission
import com.nuun.track.core.configs.state.ResultState
import com.nuun.track.domain.configs.ToolbarConfig
import com.nuun.track.domain.reservation.response.EvidenceStep
import com.nuun.track.domain.reservation.response.ReservationDomain
import com.nuun.track.navigation.screens.HomeNavScreen
import com.nuun.track.ui.components.button.CustomButton
import com.nuun.track.ui.components.media.MediaPreview
import com.nuun.track.ui.components.toolbar.CustomToolbar
import com.nuun.track.ui.shared_viewmodel.MoshiViewModel
import com.nuun.track.ui.theme.ColorBgIcon
import com.nuun.track.ui.theme.ColorContainer
import com.nuun.track.ui.theme.ColorOnline
import com.nuun.track.ui.theme.ColorTextDefault
import com.nuun.track.utility.consts.ExtraConst
import com.nuun.track.utility.enums.FileTypes
import com.nuun.track.utility.enums.ReservationStatus
import com.nuun.track.utility.extensions.checkDevicePermission
import com.nuun.track.utility.extensions.orZero
import com.nuun.track.utility.extensions.toastShortExt
import java.io.File

@Composable
fun ReservationFormScreen(
    navController: NavController,
    moshiViewModel: MoshiViewModel = hiltViewModel(),
    reservationFormViewModel: ReservationFormViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val enablePreviewImageForm = false // Change this state to display image form
    val reservation by reservationFormViewModel.reservation.collectAsState()
    val selectedUris by reservationFormViewModel.selectedUris.collectAsState()
    val resultUpdateReservation by reservationFormViewModel.resultUpdateReservation.collectAsState()
    var currentStep by remember { mutableIntStateOf(0) }

    val steps = remember {
        mutableStateListOf(
            EvidenceStep(
                title = "Exterior Depan",
                exampleImage = R.drawable.ic_car_front
            ),
            EvidenceStep(
                title = "Exterior Belakang",
                exampleImage = R.drawable.ic_car_back
            ),
            EvidenceStep(
                title = "Exterior Kanan",
                exampleImage = R.drawable.ic_car_right
            ),
            EvidenceStep(
                title = "Exterior Kiri",
                exampleImage = R.drawable.ic_car_left
            ),
            EvidenceStep(
                title = "Interior Depan",
                exampleImage = R.drawable.ic_car_ext_front
            ),
            EvidenceStep(
                title = "Interior Belakang",
                exampleImage = R.drawable.ic_car_ext_back
            ),
        )
    }

    InitCameraPermission(context)

    LaunchedEffect(Unit) {
        val jsonString =
            navController.currentBackStackEntry?.arguments?.getString(ExtraConst.EXTRA_RESERVATION)
        reservationFormViewModel.setReservation(
            jsonString?.let { moshiViewModel.fromJson(jsonString, ReservationDomain::class.java) }
        )
    }

    LaunchedEffect(resultUpdateReservation) {
        reservationFormViewModel.clearUri()
        if (resultUpdateReservation is ResultState.Success) {
            val data = (resultUpdateReservation as ResultState.Success<ReservationDomain?>).data
            if (data != null) {
                reservation?.let {
                    navController.navigate(HomeNavScreen.ReservationDetail.createRoute(it, true)) {
                        popUpTo(HomeNavScreen.ReservationDetail.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            InitTopBar(
                context,
                navController,
                reservationFormViewModel,
            )
        },
        containerColor = ColorContainer,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.forEachIndexed { index, step ->
                    EvidenceStepHeader(
                        index,
                        step,
                        currentStep,
                        index == steps.lastIndex
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val step = steps[currentStep]
            EvidenceStepContent(
                reservationFormViewModel = reservationFormViewModel,
                context = context,
                step = step,
                isLastStep = currentStep == steps.lastIndex,
                onImageSelected = { uri ->
                    steps[currentStep] = step.copy(imageUri = uri, isCompleted = true)
                },
                onNextStep = {
                    step.imageUri?.let {
                        reservationFormViewModel.addUri(it)
                    }
                    if (step.isCompleted && currentStep < steps.lastIndex) {
                        currentStep++
                    } else {
                        val imageUris: List<Uri> = steps.mapNotNull { it.imageUri }
                        reservationFormViewModel.updateReservation(
                            context = context,
                            reservationId = reservation?.id.orZero(),
                            customerId = reservation?.customer?.id.orZero(),
                            vehicleId = reservation?.vehicle?.id.orZero(),
                            files = imageUris
                        )
                    }
                },
            )

            if (enablePreviewImageForm) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = Int.MAX_VALUE,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    itemVerticalAlignment = Alignment.CenterVertically,
                ) {
                    selectedUris.forEachIndexed { index, uri ->
                        Box {
                            MediaPreview(
                                uri = uri,
                                label = steps[index].title,
                                type = FileTypes.PHOTO
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InitCameraPermission(context: Context) {
    val isCameraPermission = remember {
        mutableStateOf(
            context.checkDevicePermission(Manifest.permission.CAMERA)
        )
    }

    CameraPermission(
        onPermissionGranted = {
            isCameraPermission.value = it
        }
    )
}

@Composable
fun InitTopBar(
    context: Context,
    navController: NavController,
    reservationFormViewModel: ReservationFormViewModel,
) {
    val isLoading by reservationFormViewModel.isLoading.collectAsState()
    CustomToolbar(
        config = ToolbarConfig(
            showTitle = true,
            title = stringResource(R.string.label_form_car_evidence),
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
fun EvidenceStepHeader(
    index: Int,
    step: EvidenceStep,
    currentStep: Int,
    isLastStep: Boolean,
) {
    Row(
        modifier = Modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    when {
                        step.isCompleted -> ColorOnline
                        index == currentStep -> ColorBgIcon
                        else -> ColorContainer
                    }
                )
                .then(
                    if (!step.isCompleted && index != currentStep) {
                        Modifier.border(
                            width = 1.dp,
                            color = ColorTextDefault,
                            shape = CircleShape
                        )
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            if (step.isCompleted) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White
                )
            } else {
                Text("${index + 1}", color = Color.White)
            }
        }
        if (!isLastStep) {
            Spacer(modifier = Modifier.width(5.dp))
            Canvas(
                modifier = Modifier
                    .height(2.dp)
                    .width(10.dp)
            ) {
                drawLine(
                    color = Color.White,
                    start = Offset(0f, size.height / 2f),            // left center
                    end = Offset(size.width, size.height / 2f),      // right center
                    strokeWidth = size.height,
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(20f, 10f), // 20px dash, 10px gap
                        0f
                    )
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
        }
    }
}

@Composable
fun EvidenceStepContent(
    reservationFormViewModel: ReservationFormViewModel,
    context: Context,
    step: EvidenceStep,
    isLastStep: Boolean,
    onImageSelected: (Uri) -> Unit,
    onNextStep: () -> Unit,
) {
    val reservation by reservationFormViewModel.reservation.collectAsState()
    val status = reservation?.reservationDetail?.status
    val isLoading by reservationFormViewModel.isLoading.collectAsState()
    val selectedUris by reservationFormViewModel.selectedUris.collectAsState()
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uris ->
        uris?.let { uri ->
            val isAlreadySelected = selectedUris.any { it.toString() == uri.toString() }
            if (!isAlreadySelected) {
                onImageSelected(uri)
            } else {
                context.toastShortExt(context.getString(R.string.label_image_already_exist))
            }
        }
    }

    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            File(context.cacheDir, "camera_photo.jpg")
        )
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            onImageSelected(photoUri)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            step.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            step.imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(step.imageUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                // Blurred background
                Image(
                    painter = painterResource(step.exampleImage),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds,
                    alpha = 0.8f
                )
                // The Image
                Image(
                    painter = painterResource(step.exampleImage),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                Text(
                    stringResource(R.string.label_example_image),
                    color = Color.White
                )
                // Blurred background for finishing example image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .clip(RoundedCornerShape(10.dp)),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .border(
                    width = 2.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            takePictureLauncher.launch(photoUri)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "Kamera",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                VerticalDivider(
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier
                        .height(32.dp)
                        .width(2.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            mediaPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_image),
                        contentDescription = "Galeri",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val buttonText = when {
            step.imageUri == null || (step.isCompleted && !isLastStep) -> stringResource(R.string.label_next)
            else -> {
                if (status == ReservationStatus.START) {
                    stringResource(R.string.label_start_trip)
                } else {
                    stringResource(R.string.label_return_confirmation)
                }
            }
        }
        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            text = buttonText,
            isLoadingEnabled = isLoading,
            isEnabled = step.imageUri != null,
            onClick = {
                onNextStep()
            },
        )
    }
}