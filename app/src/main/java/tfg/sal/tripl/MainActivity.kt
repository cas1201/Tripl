package tfg.sal.tripl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import tfg.sal.tripl.appcontent.home.itinerary.ui.ItineraryScreen
import tfg.sal.tripl.appcontent.home.itinerary.ui.ItineraryViewModel
import tfg.sal.tripl.appcontent.home.ui.HomeScreen
import tfg.sal.tripl.appcontent.home.ui.HomeViewModel
import tfg.sal.tripl.appcontent.login.domain.FireBaseViewModel
import tfg.sal.tripl.appcontent.login.ui.LoginScreen
import tfg.sal.tripl.appcontent.login.ui.LoginViewModel
import tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.detailsoptions.logininfo.ui.LoginInfoViewModel
import tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.detailsoptions.ui.LoginInfoScreen
import tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.ui.ProfileDetailsScreen
import tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.ui.ProfileDetailsViewModel
import tfg.sal.tripl.appcontent.profile.profileoptions.settings.ui.SettingsScreen
import tfg.sal.tripl.appcontent.profile.profileoptions.settings.ui.SettingsViewModel
import tfg.sal.tripl.appcontent.profile.profileoptions.support.ui.SupportScreen
import tfg.sal.tripl.appcontent.profile.profileoptions.support.ui.SupportViewModel
import tfg.sal.tripl.appcontent.profile.profileoptions.usermanual.ui.PreferencesScreen
import tfg.sal.tripl.appcontent.profile.profileoptions.usermanual.ui.UserManualViewModel
import tfg.sal.tripl.appcontent.profile.ui.ProfileScreen
import tfg.sal.tripl.appcontent.profile.ui.ProfileViewModel
import tfg.sal.tripl.appcontent.recoverpassword.ui.RecoverPasswordScreen
import tfg.sal.tripl.appcontent.recoverpassword.ui.RecoverPasswordViewModel
import tfg.sal.tripl.appcontent.signup.ui.SignUpScreen
import tfg.sal.tripl.appcontent.signup.ui.SignUpViewModel
import tfg.sal.tripl.appcontent.splash.ui.SplashScreen
import tfg.sal.tripl.appcontent.splash.ui.SplashViewModel
import tfg.sal.tripl.appcontent.trip.ui.TripScreen
import tfg.sal.tripl.appcontent.trip.ui.TripViewModel
import tfg.sal.tripl.core.Routes
import tfg.sal.tripl.theme.TriplTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val fireBaseViewModel: FireBaseViewModel by viewModels()

    private val splashViewModel: SplashViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val recoverPasswordViewModel: RecoverPasswordViewModel by viewModels()
    private val signUpViewModel: SignUpViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val itineraryViewModel: ItineraryViewModel by viewModels()
    private val tripViewModel: TripViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val profileDetailsViewModel: ProfileDetailsViewModel by viewModels()
    private val loginInfoViewModel: LoginInfoViewModel by viewModels()
    private val userManualViewModel: UserManualViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val supportViewModel: SupportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriplTheme(settingsViewModel) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navigationController = rememberNavController()
                    NavHost(
                        navController = navigationController,
                        startDestination = Routes.SplashScreen.route
                    ) {
                        composable(Routes.SplashScreen.route) {
                            SplashScreen(
                                splashViewModel,
                                homeViewModel,
                                fireBaseViewModel,
                                navigationController
                            )
                        }
                        composable(Routes.LoginScreen.route) {
                            LoginScreen(
                                loginViewModel,
                                fireBaseViewModel,
                                navigationController
                            )
                        }
                        composable(Routes.RecoverPasswordScreen.route) {
                            RecoverPasswordScreen(
                                recoverPasswordViewModel,
                                fireBaseViewModel,
                                navigationController
                            )
                        }
                        composable(Routes.SignUpScreen.route) {
                            SignUpScreen(
                                signUpViewModel,
                                fireBaseViewModel,
                                navigationController
                            )
                        }
                        composable(Routes.HomeScreen.route) {
                            HomeScreen(
                                homeViewModel,
                                itineraryViewModel,
                                navigationController
                            )
                        }
                        composable(Routes.ItineraryScreen.route) {
                            ItineraryScreen(
                                itineraryViewModel,
                                tripViewModel,
                                navigationController
                            )
                        }
                        composable(Routes.TripScreen.route) {
                            TripScreen(
                                tripViewModel,
                                itineraryViewModel,
                                navigationController
                            )
                        }
                        composable(Routes.ProfileScreen.route) {
                            ProfileScreen(
                                profileViewModel,
                                settingsViewModel,
                                fireBaseViewModel,
                                navigationController
                            )
                        }
                        composable(Routes.ProfileDetailsScreen.route) {
                            ProfileDetailsScreen(
                                profileDetailsViewModel,
                                fireBaseViewModel,
                                navigationController
                            )
                        }
                        composable(Routes.LoginInfoScreen.route) {
                            LoginInfoScreen(
                                loginInfoViewModel,
                                settingsViewModel,
                                fireBaseViewModel,
                                navigationController
                            )
                        }
                        composable(Routes.UserManualScreen.route) {
                            PreferencesScreen(
                                userManualViewModel,
                                navigationController
                            )
                        }
                        composable(Routes.SettingsScreen.route) {
                            SettingsScreen(
                                settingsViewModel,
                                navigationController
                            )
                        }
                        composable(Routes.SupportScreen.route) {
                            SupportScreen(
                                supportViewModel,
                                fireBaseViewModel,
                                navigationController
                            )
                        }
                    }
                }
            }
        }
    }
}