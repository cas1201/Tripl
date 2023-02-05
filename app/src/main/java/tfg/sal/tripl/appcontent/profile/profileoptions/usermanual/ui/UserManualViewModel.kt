package tfg.sal.tripl.appcontent.profile.profileoptions.usermanual.ui

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import tfg.sal.tripl.core.Routes

class UserManualViewModel : ViewModel() {
    fun onBackPressed(navigationController: NavHostController) {
        navigationController.navigate(Routes.ProfileScreen.route) {
            popUpTo(Routes.PreferencesScreen.route) { inclusive = true }
        }
    }
}