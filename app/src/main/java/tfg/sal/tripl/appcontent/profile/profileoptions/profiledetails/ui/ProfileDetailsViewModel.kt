package tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.ui

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.login.domain.FireBaseViewModel
import tfg.sal.tripl.core.Routes

class ProfileDetailsViewModel : ViewModel() {

    fun onBackPressed(navigationController: NavHostController) {
        navigationController.navigate(Routes.ProfileScreen.route) {
            popUpTo(Routes.ProfileScreen.route) { inclusive = true }
        }
    }

    fun navigate(destination: Int, navigationController: NavHostController) {
        when (destination) {
            R.string.login_info -> {
                navigationController.navigate(Routes.LoginInfoScreen.route)
            }
            R.string.traveler_info -> {
                navigationController.navigate(Routes.TravelerInfoScreen.route)
            }
            R.string.payments -> {
                navigationController.navigate(Routes.PaymentsScreen.route)
            }
        }
    }

    fun logout(fireBaseViewModel: FireBaseViewModel?, navigationController: NavHostController) {
        fireBaseViewModel?.logout()
        navigationController.navigate(Routes.LoginScreen.route) {
            popUpTo(Routes.LoginScreen.route) { inclusive = false }
        }
    }
}