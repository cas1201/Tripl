package tfg.sal.tripl.appcontent.splash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.home.ui.HomeViewModel
import tfg.sal.tripl.appcontent.login.domain.FireBaseViewModel
import tfg.sal.tripl.theme.SecondaryColor

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    homeViewModel: HomeViewModel,
    fireBaseViewModel: FireBaseViewModel?,
    navigationController: NavHostController
) {
    val continueToHome: Boolean by viewModel.continueToHome.observeAsState(initial = false)
    viewModel.getCountries(homeViewModel)

    LaunchedEffect(key1 = continueToHome) {
        viewModel.initializeUser(
            fireBaseViewModel?.currentUser,
            continueToHome,
            navigationController
        )
    }
    Splash()
}

@Composable
fun Splash() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.logo),
            modifier = Modifier.size(150.dp)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier.padding(vertical = 16.dp),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = SecondaryColor
        )
        CircularProgressIndicator(color = SecondaryColor)
    }
}