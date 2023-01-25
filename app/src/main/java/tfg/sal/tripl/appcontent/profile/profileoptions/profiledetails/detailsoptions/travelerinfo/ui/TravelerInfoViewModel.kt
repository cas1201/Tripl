package tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.detailsoptions.travelerinfo.ui

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import tfg.sal.tripl.core.Routes

class TravelerInfoViewModel : ViewModel() {
    fun onBackPressed(navigationController: NavHostController) {
        navigationController.navigate(Routes.ProfileDetailsScreen.route) {
            popUpTo(Routes.ProfileDetailsScreen.route) { inclusive = true }
        }
    }
}