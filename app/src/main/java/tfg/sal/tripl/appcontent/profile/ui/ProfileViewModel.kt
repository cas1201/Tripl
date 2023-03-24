package tfg.sal.tripl.appcontent.profile.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.login.domain.FireBaseViewModel
import tfg.sal.tripl.appcontent.profile.domain.GridModal
import tfg.sal.tripl.core.Routes
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _showAlertDialog = MutableLiveData<Boolean>()
    val showAlertDialog: LiveData<Boolean> = _showAlertDialog

    fun profileList(): ArrayList<GridModal> {
        var profileList = ArrayList<GridModal>()

        profileList = (profileList + GridModal(
            R.string.profile_details,
            R.drawable.profile_details_vector
        )) as ArrayList<GridModal>
        profileList = (profileList + GridModal(
            R.string.user_manual,
            R.drawable.user_manual_vector
        )) as ArrayList<GridModal>
        profileList = (profileList + GridModal(
            R.string.settings,
            R.drawable.settings_vector
        )) as ArrayList<GridModal>
        profileList = (profileList + GridModal(
            R.string.support,
            R.drawable.support_vector
        )) as ArrayList<GridModal>
        return profileList
    }

    fun onCardClick(optionName: Int, navigationController: NavHostController) {
        when (optionName) {
            R.string.profile_details -> navigationController.navigate(Routes.UserInfoScreen.route)
            R.string.settings -> navigationController.navigate(Routes.SettingsScreen.route)
            R.string.user_manual -> navigationController.navigate(Routes.UserManualScreen.route)
            R.string.support -> navigationController.navigate(Routes.SupportScreen.route)
        }
    }

    fun onIndexChange(bottomIndex: Int, navigationController: NavHostController) {
        when (bottomIndex) {
            1 -> navigationController.navigate(Routes.HomeScreen.route)
            2 -> navigationController.navigate(Routes.TripScreen.route)
            3 -> navigationController.navigate(Routes.ProfileScreen.route)
        }
    }

    fun logOutClicked(show: Boolean) {
        _showAlertDialog.value = show
    }

    fun logout(fireBaseViewModel: FireBaseViewModel?, navigationController: NavHostController) {
        fireBaseViewModel?.logout()
        navigationController.navigate(Routes.LoginScreen.route) {
            popUpTo(Routes.LoginScreen.route) { inclusive = false }
        }
    }
}