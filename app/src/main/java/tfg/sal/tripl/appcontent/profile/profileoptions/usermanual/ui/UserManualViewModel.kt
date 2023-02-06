package tfg.sal.tripl.appcontent.profile.profileoptions.usermanual.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import tfg.sal.tripl.core.Routes

class UserManualViewModel : ViewModel() {
    fun onBackPressed(navigationController: NavHostController) {
        navigationController.navigate(Routes.ProfileScreen.route) {
            popUpTo(Routes.UserManualScreen.route) { inclusive = true }
        }
    }

    fun showToast(context: Context, message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun pdfRenderError(navigationController: NavHostController) {
        navigationController.navigate(Routes.ProfileScreen.route) {
            popUpTo(Routes.UserManualScreen.route)
        }
    }
}