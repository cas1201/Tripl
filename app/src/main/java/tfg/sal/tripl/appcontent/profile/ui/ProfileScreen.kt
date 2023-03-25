package tfg.sal.tripl.appcontent.profile.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.home.ui.BottomNav
import tfg.sal.tripl.appcontent.login.domain.FireBaseViewModel
import tfg.sal.tripl.appcontent.login.ui.TriplButton
import tfg.sal.tripl.appcontent.login.ui.headerText
import tfg.sal.tripl.appcontent.profile.domain.GridModal
import tfg.sal.tripl.theme.SecondaryColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    fireBaseViewModel: FireBaseViewModel,
    navigationController: NavHostController
) {
    val showAlertDialog: Boolean by viewModel.showAlertDialog.observeAsState(initial = false)
    val profileList = viewModel.profileList()

    val scaffoldState = rememberScaffoldState()
    val currentDestination = navigationController.currentDestination

    if (showAlertDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.logOutClicked(!showAlertDialog) },
            title = {
                Text(text = stringResource(id = R.string.log_out_dialog))
            },
            text = {
                Text(text = stringResource(id = R.string.log_out_dialog_text))
            },
            buttons = {
                Row(Modifier.padding(25.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    TriplButton(
                        text = stringResource(id = R.string.alert_dialog_yes),
                        buttonEnable = true,
                        alertDialog = true
                    ) {
                        viewModel.logOutClicked(!showAlertDialog)
                        viewModel.logout(fireBaseViewModel, navigationController)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TriplButton(
                        text = stringResource(id = R.string.alert_dialog_no),
                        buttonEnable = true,
                        alertDialog = true
                    ) { viewModel.logOutClicked(!showAlertDialog) }
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.padding(16.dp),
        scaffoldState = scaffoldState,
        topBar = {
            ProfileHeader(
                modifier = Modifier.padding(start = 16.dp),
                viewModel,
                showAlertDialog
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
fun ProfileHeader(modifier: Modifier, viewModel: ProfileViewModel, showAlertDialog: Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        headerText(
            size = 30,
            text = stringResource(id = R.string.profile),
            modifier = modifier
        )
        Icon(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .clickable { viewModel.logOutClicked(!showAlertDialog) },
            imageVector = Icons.Default.PowerSettingsNew,
            contentDescription = stringResource(id = R.string.log_out),
            tint = SecondaryColor
        )
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
