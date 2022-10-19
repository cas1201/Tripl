package tfg.sal.tripl.ui.profile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import tfg.sal.tripl.ui.home.ui.BottomNav
import tfg.sal.tripl.ui.login.ui.headerText
import tfg.sal.tripl.R

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, navigationController: NavHostController) {
    val scaffoldState = rememberScaffoldState()
    val currentDestination = navigationController.currentDestination
    Scaffold(
        modifier = Modifier.padding(16.dp),
        scaffoldState = scaffoldState,
        topBar = {
            ProfileHeader(modifier = Modifier.padding(16.dp))
        },
        content = { ProfileContent(modifier = Modifier.padding(16.dp)) },
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
fun ProfileHeader(modifier: Modifier) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        headerText(
            size = 30,
            text = stringResource(id = R.string.profile),
            modifier = modifier
        )
        Box(modifier = modifier) {
            Text(
                text = "SL",
                textAlign = TextAlign.Center,
                fontSize = 35.sp,
                color = Color.Black,
                modifier = modifier
                    .size(45.dp)
                    .align(Alignment.Center)
                    .drawBehind {
                        drawCircle(
                            color = Color(0xFF00897b),
                            radius = this.size.maxDimension
                        )
                    }
            )
        }
    }
}

@Composable
fun ProfileContent(modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.manage_your_account)
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Column(modifier = Modifier.padding(4.dp)) {
                Image(
                    modifier = modifier.size(50.dp),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(id = R.string.profile_details)
                )
                Text(text = stringResource(id = R.string.profile_details))
            }
            Column(modifier = Modifier.padding(4.dp)) {
                Image(
                    modifier = modifier.size(50.dp),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(id = R.string.preferences)
                )
                Text(text = stringResource(id = R.string.preferences))
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Column(modifier = Modifier.padding(4.dp)) {
                Image(
                    modifier = modifier.size(50.dp),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(id = R.string.settings)
                )
                Text(text = stringResource(id = R.string.settings))
            }
            Column(modifier = Modifier.padding(4.dp)) {
                Image(
                    modifier = modifier.size(50.dp),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(id = R.string.support)
                )
                Text(text = stringResource(id = R.string.support))
            }
        }
    }
}
