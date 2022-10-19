package tfg.sal.tripl.ui.splash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import tfg.sal.tripl.model.Routes
import tfg.sal.tripl.R

@Composable
fun SplashScreen(
    //firebaseAuth: FirebaseAuth,
    navigationController: NavHostController
) {
    //val loggedUser = firebaseAuth.currentUser != null

    LaunchedEffect(key1 = true) {
        /*if (loggedUser) {
            navigationController.popBackStack()
            navigationController.navigate(Routes.HomeScreen.route)
        } else {*/
            navigationController.popBackStack()
            navigationController.navigate(Routes.LoginScreen.route)
        //}
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
            color = Color(0xFF00897b)
        )
    }
}