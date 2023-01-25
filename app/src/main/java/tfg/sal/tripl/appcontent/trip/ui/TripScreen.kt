package tfg.sal.tripl.appcontent.trip.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.home.ui.BottomNav
import tfg.sal.tripl.appcontent.login.data.network.FireBaseViewModel
import tfg.sal.tripl.appcontent.login.ui.headerText

@Composable
fun TripScreen(
    viewModel: TripViewModel,
    fireBaseViewModel: FireBaseViewModel,
    navigationController: NavHostController
) {
    val scaffoldState = rememberScaffoldState()
    val currentDestination = navigationController.currentDestination
    Scaffold(
        modifier = Modifier.padding(16.dp),
        scaffoldState = scaffoldState,
        topBar = {
            headerText(
                size = 30,
                text = stringResource(id = R.string.trip),
                modifier = Modifier.padding(start = 16.dp)
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
    ) {

    }
}