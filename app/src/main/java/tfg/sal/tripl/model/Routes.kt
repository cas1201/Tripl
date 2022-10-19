package tfg.sal.tripl.model

sealed class Routes(val route: String) {
    object SplashScreen : Routes("splashScreen")
    object LoginScreen : Routes("loginScreen")
    object PasswordForgetScreen : Routes("passwordForgetScreen")
    object SignUpScreen : Routes("signupScreen")
    object HomeScreen : Routes("homeScreen")
    object TripScreen : Routes("tripScreen")
    object ProfileScreen : Routes("profileScreen")
}