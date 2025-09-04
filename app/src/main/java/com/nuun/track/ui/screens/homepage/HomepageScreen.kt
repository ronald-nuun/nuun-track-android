package com.nuun.track.ui.screens.homepage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nuun.track.R
import com.nuun.track.core.configs.networking.TokenManager
import com.nuun.track.core.configs.state.ResultState
import com.nuun.track.domain.configs.TextFieldConfig
import com.nuun.track.domain.reservation.response.ReservationDomain
import com.nuun.track.ui.common.HandleErrorStates
import com.nuun.track.ui.components.button.CustomButton
import com.nuun.track.ui.components.cards.CardReservation
import com.nuun.track.ui.components.cards.CardShimmer
import com.nuun.track.ui.components.forms.TextFieldWithIcons
import com.nuun.track.ui.screens.LocalTokenManager
import com.nuun.track.ui.shared_viewmodel.EncryptedPrefViewModel
import com.nuun.track.ui.shared_viewmodel.PrefDataStoreViewModel
import com.nuun.track.ui.theme.ColorTextDefault
import com.nuun.track.ui.theme.Neutral100
import com.nuun.track.ui.theme.Neutral1000
import com.nuun.track.ui.theme.Neutral1100
import com.nuun.track.utility.utils.logout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomepageScreen(
    navController: NavController,
    homepageViewModel: HomepageViewModel = hiltViewModel(),
    encryptedPrefViewModel: EncryptedPrefViewModel = hiltViewModel(),
    prefViewModel: PrefDataStoreViewModel = hiltViewModel(),
) {
    val tokenManager = LocalTokenManager.current

    val refreshState = rememberPullToRefreshState()
    val pageViewState by homepageViewModel.pageViewState.collectAsState()
    val isRefreshing by homepageViewModel.isRefreshing.collectAsState()

    val reservationState = remember {
        mutableStateOf<ResultState<List<ReservationDomain>?>>(ResultState.Success(null))
    }

    LaunchedEffect(Unit) {
        homepageViewModel.getRecentReservation()
        homepageViewModel.resultReservation.collect { result ->
            reservationState.value = result
        }
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            homepageViewModel.getRecentReservation()
            homepageViewModel.updateIsRefreshing(false)
        }
    }

    Scaffold(
        containerColor = Neutral1000,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .padding(horizontal = 10.dp)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.label_find_transaction),
                color = ColorTextDefault,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            SearchTextField(
                homepageViewModel
            )
            Spacer(modifier = Modifier.height(18.dp))
            PullToRefreshBox(
                state = refreshState,
                onRefresh = { homepageViewModel.updateIsRefreshing(true) },
                isRefreshing = isRefreshing,
            ) {
                pageViewState
                    .onLoading {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .weight(1f),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(10) {
                                    CardShimmer(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .height(90.dp)
                                    )
                                }
                            }
                            CustomButton(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.label_logout),
                                onClick = {
                                    logout(
                                        prefViewModel = prefViewModel,
                                        encryptedPrefViewModel = encryptedPrefViewModel,
                                        tokenManager = tokenManager,
                                        navController = navController
                                    )
                                },
                            )
                        }
                    }
                    .onSuccessData { data ->
                        homepageViewModel.setReservationList(data)
                    }
                    .onSuccess {
                        ShowReservationList(
                            navController,
                            homepageViewModel,
                            prefViewModel,
                            tokenManager,
                            encryptedPrefViewModel
                        )
                    }
                    .onEmpty {
                        NoData(
                            prefViewModel,
                            encryptedPrefViewModel,
                            tokenManager,
                            navController
                        )
                    }
                    .onError { error ->
                        HandleErrorStates(
                            prefViewModel = prefViewModel,
                            encryptedPrefViewModel = encryptedPrefViewModel,
                            navController = navController,
                            states = arrayOf(
                                ResultState.Error(error)
                            )
                        )
                        NoData(
                            prefViewModel,
                            encryptedPrefViewModel,
                            tokenManager,
                            navController,
                            if (error.message != null)
                                error.message.toString()
                            else stringResource(
                                id = R.string.error_no_data
                            )
                        )
                    }
            }
        }
    }
}

@Composable
fun NoData(
    prefViewModel: PrefDataStoreViewModel,
    encryptedPrefViewModel: EncryptedPrefViewModel,
    tokenManager: TokenManager,
    navController: NavController,
    message: String = stringResource(id = R.string.error_no_data)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = ColorTextDefault,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.label_logout),
            onClick = {
                logout(
                    prefViewModel = prefViewModel,
                    encryptedPrefViewModel = encryptedPrefViewModel,
                    tokenManager = tokenManager,
                    navController = navController
                )
            },
        )
    }
}

@Composable
fun SearchTextField(
    homepageViewModel: HomepageViewModel
) {
    val query by homepageViewModel.query.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val config = TextFieldConfig(
        textPlaceholder = stringResource(id = R.string.hint_enter_transaction_id),
        errorMessage = stringResource(R.string.warning_email_not_valid),
        isError = false,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        backgroundColor = Neutral100,
        cursorColor = Neutral1100,
        textColor = Neutral1100,
        roundedSize = 12.dp,
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                homepageViewModel.getFilteredReservation(query)
            }
        ),
        autoShowClear = true
    )

    TextFieldWithIcons(
        value = query,
        onValueChange = {
            homepageViewModel.setQuery(it)
        },
        modifier = Modifier.fillMaxWidth(),
        config = config
    )
}

@Composable
fun ShowReservationList(
    navController: NavController,
    homepageViewModel: HomepageViewModel,
    prefViewModel: PrefDataStoreViewModel,
    tokenManager: TokenManager,
    encryptedPrefViewModel: EncryptedPrefViewModel,
) {
    val reservationList by homepageViewModel.reservationList.collectAsState()
    Column {
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(reservationList) { reservation ->
                CardReservation(
                    navController = navController,
                    modifier = Modifier.fillMaxWidth(),
                    reservation = reservation,
                )
            }
        }
        CustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            text = stringResource(R.string.label_logout),
            onClick = {
                logout(
                    prefViewModel = prefViewModel,
                    encryptedPrefViewModel = encryptedPrefViewModel,
                    tokenManager = tokenManager,
                    navController = navController
                )
            },
        )
    }
}