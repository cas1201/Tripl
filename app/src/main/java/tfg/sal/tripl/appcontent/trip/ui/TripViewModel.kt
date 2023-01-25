package tfg.sal.tripl.appcontent.trip.ui

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import tfg.sal.tripl.core.Routes

class TripViewModel : ViewModel() {

    fun onIndexChange(bottomIndex: Int, navigationController: NavHostController) {
        when (bottomIndex) {
            1 -> navigationController.navigate(Routes.HomeScreen.route)
            2 -> navigationController.navigate(Routes.TripScreen.route)
            3 -> navigationController.navigate(Routes.ProfileScreen.route)
        }
    }
}