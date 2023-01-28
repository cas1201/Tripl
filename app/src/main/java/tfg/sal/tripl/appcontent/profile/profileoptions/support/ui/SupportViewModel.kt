package tfg.sal.tripl.appcontent.profile.profileoptions.support.ui

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import tfg.sal.tripl.core.Routes

class SupportViewModel : ViewModel() {
    fun onBackPressed(navigationController: NavHostController) {
        navigationController.navigate(Routes.ProfileScreen.route) {
            popUpTo(Routes.SupportScreen.route) { inclusive = true }
        }
    }
    fun sendEmail(supportEmail: String) {
        // open gmail to send an email
    }
}