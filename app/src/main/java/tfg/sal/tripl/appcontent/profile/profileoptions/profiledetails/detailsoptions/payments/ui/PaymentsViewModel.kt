package tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.detailsoptions.payments.ui

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import tfg.sal.tripl.core.Routes

class PaymentsViewModel : ViewModel() {
    fun onBackPressed(navigationController: NavHostController) {
        navigationController.navigate(Routes.ProfileDetailsScreen.route) {
            popUpTo(Routes.ProfileDetailsScreen.route) { inclusive = true }
        }
    }
}