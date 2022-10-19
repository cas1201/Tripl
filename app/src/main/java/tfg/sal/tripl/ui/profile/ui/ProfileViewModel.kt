package tfg.sal.tripl.ui.profile.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import tfg.sal.tripl.model.Routes
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(): ViewModel() {

    fun onIndexChange(bottomIndex: Int, navigationController: NavHostController) {
        when (bottomIndex) {
            1 -> {
                navigationController.navigate(Routes.HomeScreen.route)
            }
            2 -> {
                navigationController.navigate(Routes.TripScreen.route)
            }
            3 -> {
                navigationController.navigate(Routes.ProfileScreen.route)
            }
        }
    }
}