package tfg.sal.tripl.appcontent.profile.profileoptions.preferences.ui

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import tfg.sal.tripl.core.Routes

class PreferencesViewModel : ViewModel() {
    fun onBackPressed(navigationController: NavHostController) {
        navigationController.navigate(Routes.ProfileScreen.route) {
            popUpTo(Routes.PreferencesScreen.route) { inclusive = true }
        }
    }
}