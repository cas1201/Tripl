package tfg.sal.tripl.appcontent.profile.profileoptions.settings.ui

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import tfg.sal.tripl.core.Routes

class SettingsViewModel : ViewModel() {
    fun onBackPressed(navigationController: NavHostController) {
        navigationController.navigate(Routes.ProfileScreen.route) {
            popUpTo(Routes.SettingsScreen.route) { inclusive = true }
        }
    }
}