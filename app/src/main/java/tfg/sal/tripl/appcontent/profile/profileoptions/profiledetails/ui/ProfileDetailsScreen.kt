package tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.login.domain.FireBaseViewModel
import tfg.sal.tripl.theme.SecondaryColor

@Composable
fun ProfileDetailsScreen(
    viewModel: ProfileDetailsViewModel,
    fireBaseViewModel: FireBaseViewModel?,
    navigationController: NavHostController
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BackHeader {
            viewModel.onBackPressed(
                navigationController
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        ProfileDetailsBody(viewModel, fireBaseViewModel, navigationController)
    }
}

@Composable
fun BackHeader(onBackPressed: () -> Unit) {
    Icon(
        imageVector = Icons.Default.ArrowBack,
        contentDescription = stringResource(id = R.string.back),
        modifier = Modifier.clickable { onBackPressed() }
    )
}

@Composable
fun ProfileDetailsBody(
    viewModel: ProfileDetailsViewModel,
    fireBaseViewModel: FireBaseViewModel?,
    navigationController: NavHostController
) {
    Column(modifier = Modifier) {
        Text(
            text = stringResource(id = R.string.your_details),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.padding(16.dp))
        DetailsRow(
            stringResource(id = R.string.login_info),
            clickAction = { viewModel.navigate(R.string.login_info, navigationController) })
        DetailsRow(stringResource(id = R.string.log_out), false) {
            viewModel.logout(
                fireBaseViewModel,
                navigationController
            )
        }
    }
}

@Composable
fun DetailsRow(text: String, icon: Boolean = true, clickAction: () -> Unit) {
    Column(Modifier.clickable { clickAction() }) {
        Divisor()
        Spacer(modifier = Modifier.padding(8.dp))
        Row {
            if (icon) {
                Text(text = text)
            } else {
                Text(text = text, color = SecondaryColor, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.weight(1f))
            if (icon) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = stringResource(id = R.string.forward)
                )
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))
        if (!icon) {
            Divisor()
        }
    }
}

@Composable
fun Divisor() {
    Divider(
        Modifier
            .height(1.dp)
            .fillMaxWidth()
    )
}