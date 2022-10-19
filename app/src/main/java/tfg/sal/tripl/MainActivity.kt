package tfg.sal.tripl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import tfg.sal.tripl.model.Routes
import tfg.sal.tripl.ui.home.ui.HomeScreen
import tfg.sal.tripl.ui.home.ui.HomeViewModel
import tfg.sal.tripl.ui.login.ui.LoginScreen
import tfg.sal.tripl.ui.login.ui.LoginViewModel
import tfg.sal.tripl.ui.profile.ui.ProfileScreen
import tfg.sal.tripl.ui.profile.ui.ProfileViewModel
import tfg.sal.tripl.ui.recoverpassword.ui.RecoverPasswordScreen
import tfg.sal.tripl.ui.recoverpassword.ui.RecoverPasswordViewModel
import tfg.sal.tripl.ui.signup.ui.SignUpScreen
import tfg.sal.tripl.ui.signup.ui.SignUpViewModel
import tfg.sal.tripl.ui.splash.ui.SplashScreen
import tfg.sal.tripl.ui.theme.TriplTheme
import tfg.sal.tripl.ui.trip.ui.TripScreen
import tfg.sal.tripl.ui.trip.ui.TripViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriplTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navigationController = rememberNavController()
                    NavHost(
                        navController = navigationController,
                        startDestination = Routes.HomeScreen.route
                    ) {
                        composable(Routes.SplashScreen.route) {
                            SplashScreen(
                                navigationController
                            )
                        }
                        composable(Routes.LoginScreen.route) {
                            LoginScreen(
                                LoginViewModel(),
                                navigationController
                            )
                        }
                        composable(Routes.PasswordForgetScreen.route) {
                            RecoverPasswordScreen(
                                RecoverPasswordViewModel(),
                                navigationController
                            )
                        }
                        composable(Routes.SignUpScreen.route) {
                            SignUpScreen(
                                SignUpViewModel(),
                                navigationController
                            )
                        }
                        composable(Routes.HomeScreen.route) {
                            HomeScreen(
                                HomeViewModel(),
                                navigationController
                            )
                        }
                        composable(Routes.TripScreen.route) {
                            TripScreen(
                                TripViewModel(),
                                navigationController
                            )
                        }
                        composable(Routes.ProfileScreen.route) {
                            ProfileScreen(
                                ProfileViewModel(),
                                navigationController
                            )
                        }
                    }
                }
            }
        }
    }
}