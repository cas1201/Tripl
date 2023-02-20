package tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.detailsoptions.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseUser
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.login.domain.FireBaseViewModel
import tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.detailsoptions.logininfo.ui.LoginInfoViewModel
import tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.ui.BackHeader
import tfg.sal.tripl.appcontent.profile.profileoptions.settings.ui.SettingsViewModel
import tfg.sal.tripl.theme.PrimaryColorDay
import tfg.sal.tripl.theme.PrimaryColorNight
import tfg.sal.tripl.theme.SecondaryColor
import tfg.sal.tripl.theme.SecondaryTextColor

@Composable
fun LoginInfoScreen(
    viewModel: LoginInfoViewModel,
    settingsViewModel: SettingsViewModel,
    fireBaseViewModel: FireBaseViewModel?,
    navigationController: NavHostController
) {
    val user = fireBaseViewModel?.currentUser

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
        LoginInfoBody(Modifier.padding(16.dp), viewModel, settingsViewModel, user)
    }
}

@Composable
fun LoginInfoBody(
    modifier: Modifier,
    viewModel: LoginInfoViewModel,
    settingsViewModel: SettingsViewModel,
    user: FirebaseUser?
) {
    val userName = viewModel.getUserName(user?.displayName ?: "")
    val color =
        if (settingsViewModel.darkMode.value == "enabled") PrimaryColorNight else PrimaryColorDay

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Canvas(Modifier.size(180.dp)) {
                drawCircle(SolidColor(SecondaryColor))
            }
            Text(
                text = userName.uppercase(),
                fontSize = 80.sp,
                color = color
            )
        }
        Divisor()
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = stringResource(id = R.string.email_address),
                    color = SecondaryTextColor,
                    fontSize = 12.sp
                )
                Text(text = user?.email!!)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = stringResource(id = R.string.email_address)
            )
        }
    }
}

@Composable
fun Divisor() {
    Spacer(modifier = Modifier.padding(8.dp))
    Divider(
        Modifier
            .height(1.dp)
            .fillMaxWidth()
    )
    Spacer(modifier = Modifier.padding(8.dp))
}