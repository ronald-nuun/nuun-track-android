package com.nuun.track.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nuun.track.R
import com.nuun.track.domain.reservation.response.CustomerDomain
import com.nuun.track.domain.reservation.response.ReservationDomain
import com.nuun.track.navigation.screens.HomeNavScreen
import com.nuun.track.ui.theme.ColorBgNav
import com.nuun.track.ui.theme.ColorTextButton
import com.nuun.track.ui.theme.Neutral600
import com.nuun.track.utility.enums.ReservationStatus

@Composable
fun CardReservation(
    navController: NavController,
    modifier: Modifier,
    reservation: ReservationDomain
) {
    DefaultCard(
        modifier = modifier
            .clickable {
                navController.navigate(HomeNavScreen.ReservationDetail.createRoute(reservation))
            },
        bgColor = Color.White
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ){
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color = Neutral600, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_car),
                    contentDescription = stringResource(R.string.desc_image),
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            Column {
                Text(
                    text = reservation.customer?.name ?: stringResource(R.string.dummy_name),
                    style = MaterialTheme.typography.titleMedium,
                    color = ColorBgNav,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = reservation.customer?.phone ?: stringResource(R.string.dummy_phone),
                    style = MaterialTheme.typography.bodySmall,
                    color = ColorTextButton
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            val status = reservation.reservationDetail?.status
            val textColor = when (status) {
                ReservationStatus.END -> Color.Red
                else -> Color.Gray
            }

            Text(
                text = status?.name ?: "Unknown",
                style = MaterialTheme.typography.bodySmall,
                color = textColor
            )
        }
    }
}

@Preview
@Composable
fun PreviewCardReservation(){
    CardReservation(
        navController = rememberNavController(),
        modifier = Modifier,
        reservation = ReservationDomain(
            customer = CustomerDomain(
                id = 1,
                name = "Customer Name",
                email = "Customer Email",
                phone = "Customer Phone"
            ),
            id = 1
        )
    )
}