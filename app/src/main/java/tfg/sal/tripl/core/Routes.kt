package tfg.sal.tripl.core

sealed class Routes(val route: String) {
    object SplashScreen : Routes("splashScreen")
    object LoginScreen : Routes("loginScreen")
    object RecoverPasswordScreen : Routes("recoverPasswordScreen")
    object SignUpScreen : Routes("signupScreen")
    object HomeScreen : Routes("homeScreen")
    object ItineraryScreen : Routes("itineraryScreen")
    object TripScreen : Routes("tripScreen")
    object ProfileScreen : Routes("profileScreen")
    object UserInfoScreen : Routes("userInfoScreen")
    object TravelerInfoScreen : Routes("travelerInfoScreen")
    object PaymentsScreen : Routes("paymentsScreen")
    object UserManualScreen : Routes("preferencesScreen")
    object SettingsScreen : Routes("settingsScreen")
    object SupportScreen : Routes("supportScreen")
}