package tfg.sal.tripl.appcontent.profile.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseUser
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.home.ui.BottomNav
import tfg.sal.tripl.appcontent.login.domain.FireBaseViewModel
import tfg.sal.tripl.appcontent.login.ui.headerText
import tfg.sal.tripl.appcontent.profile.domain.GridModal
import tfg.sal.tripl.appcontent.profile.profileoptions.settings.ui.SettingsViewModel
import tfg.sal.tripl.theme.PrimaryColorDay
import tfg.sal.tripl.theme.PrimaryColorNight
import tfg.sal.tripl.theme.SecondaryColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    settingsViewModel: SettingsViewModel,
    fireBaseViewModel: FireBaseViewModel?,
    navigationController: NavHostController
) {
    val user = fireBaseViewModel?.currentUser
    val profileList = viewModel.profileList()

    val scaffoldState = rememberScaffoldState()
    val currentDestination = navigationController.currentDestination
    Scaffold(
        modifier = Modifier.padding(16.dp),
        scaffoldState = scaffoldState,
        topBar = {
            ProfileHeader(
                modifier = Modifier.padding(start = 16.dp),
                viewModel,
                settingsViewModel,
                user
            )
        },
        content = {
            ProfileContent(
                modifier = Modifier.padding(16.dp),
                profileList = profileList,
                viewModel = viewModel,
                navigationController = navigationController
            )
        },
        bottomBar = {
            BottomNav(
                currentDestination
            ) {
                viewModel.onIndexChange(
                    bottomIndex = it,
                    navigationController = navigationController
                )
            }
        }
    )
}

@Composable
fun ProfileHeader(
    modifier: Modifier,
    viewModel: ProfileViewModel,
    settingsViewModel: SettingsViewModel,
    user: FirebaseUser?
) {
    val userName = viewModel.getUserName(user?.displayName ?: "")
    val color =
        if (settingsViewModel.darkMode.value == "enabled") PrimaryColorNight else PrimaryColorDay

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        headerText(
            size = 30,
            text = stringResource(id = R.string.profile),
            modifier = modifier
        )
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Canvas(Modifier.size(95.dp)) {
                drawCircle(SolidColor(SecondaryColor))
            }
            Text(
                text = userName.uppercase(),
                fontSize = 35.sp,
                color = color
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileContent(
    modifier: Modifier,
    profileList: ArrayList<GridModal>,
    viewModel: ProfileViewModel,
    navigationController: NavHostController
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.manage_your_account),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 23.sp
        )
        Spacer(modifier = Modifier.padding(8.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(profileList.size) {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(130.dp),
                    elevation = 10.dp,
                    onClick = {
                        viewModel.onCardClick(
                            optionName = profileList[it].optionName,
                            navigationController = navigationController
                        )
                    }) {
                    Column(
                        modifier = Modifier.padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = modifier.size(50.dp),
                            painter = painterResource(id = profileList[it].optionImage),
                            contentDescription = stringResource(id = profileList[it].optionName)
                        )
                        Text(
                            text = stringResource(id = profileList[it].optionName),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
