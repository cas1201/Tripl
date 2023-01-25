package tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.detailsoptions.logininfo.ui

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import tfg.sal.tripl.core.Routes

class LoginInfoViewModel : ViewModel() {

    fun onBackPressed(navigationController: NavHostController) {
        navigationController.navigate(Routes.ProfileDetailsScreen.route) {
            popUpTo(Routes.ProfileDetailsScreen.route) { inclusive = true }
        }
    }

    fun getUserName(nameAndSurname: String): String {
        return if (nameAndSurname != "") {
            val sepString = nameAndSurname.split(" ")
            val name = sepString[0]
            val surname = sepString[1]
            "${name[0]}${surname[0]}"
        } else {
            ""
        }
    }
}