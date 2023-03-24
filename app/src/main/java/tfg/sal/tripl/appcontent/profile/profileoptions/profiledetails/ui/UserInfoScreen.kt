package tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.login.domain.FireBaseViewModel
import tfg.sal.tripl.appcontent.profile.profileoptions.settings.ui.BackHeader
import tfg.sal.tripl.appcontent.profile.profileoptions.settings.ui.SettingsViewModel
import tfg.sal.tripl.appcontent.signup.data.User
import tfg.sal.tripl.theme.PrimaryColorDay
import tfg.sal.tripl.theme.PrimaryColorNight
import tfg.sal.tripl.theme.SecondaryColor
import tfg.sal.tripl.theme.SecondaryTextColor

@Composable
fun UserInfoScreen(
    viewModel: UserInfoViewModel,
    fireBaseViewModel: FireBaseViewModel,
    settingsViewModel: SettingsViewModel,
    navigationController: NavHostController
) {
    viewModel.getUserData(LocalContext.current)
    val userData: User by viewModel.userData.observeAsState(initial = User())

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
        LoginInfoBody(
            Modifier.padding(16.dp),
            viewModel,
            fireBaseViewModel,
            settingsViewModel,
            userData
        )
    }
}

@Composable
fun LoginInfoBody(
    modifier: Modifier,
    viewModel: UserInfoViewModel,
    fireBaseViewModel: FireBaseViewModel,
    settingsViewModel: SettingsViewModel,
    userData: User
) {
    val context = LocalContext.current
    val color =
        if (settingsViewModel.darkMode.value == "enabled") PrimaryColorNight else PrimaryColorDay

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!userData.name.isNullOrBlank() && !userData.surname.isNullOrBlank()) {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                Canvas(Modifier.size(180.dp)) {
                    drawCircle(SolidColor(SecondaryColor))
                }
                Text(
                    text = "${userData.name[0].uppercase()}${userData.surname[0].uppercase()}",
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
                    Text(text = userData.email!!)
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = stringResource(id = R.string.email_address)
                )
            }
            Divisor()
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.name),
                    color = SecondaryTextColor,
                    fontSize = 12.sp
                )
                Text(text = userData.name.capitalize())
            }
            Divisor()
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.surname),
                    color = SecondaryTextColor,
                    fontSize = 12.sp
                )
                Text(
                    text = userData.surname.split(" ")
                        .joinToString(separator = " ", transform = String::capitalize)
                )
            }
            Divisor()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    fireBaseViewModel.changePassword(viewModel.getEmail(context))
                }
            ) {
                Text(
                    text = stringResource(id = R.string.change_password),
                    fontWeight = FontWeight.Bold,
                    color = SecondaryColor
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = stringResource(id = R.string.change_password)
                )
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = SecondaryColor)
            }
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