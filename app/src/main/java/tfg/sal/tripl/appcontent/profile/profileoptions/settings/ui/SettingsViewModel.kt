package tfg.sal.tripl.appcontent.profile.profileoptions.settings.ui

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.preference.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.home.itinerary.data.POITypes
import tfg.sal.tripl.core.Routes
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(app: Application) : ViewModel() {

    private val sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(app.applicationContext)
    private val editor = sharedPreferences.edit()

    private val _switchChecked = MutableLiveData<Boolean>()
    val switchChecked: LiveData<Boolean> = _switchChecked

    private val _darkMode = MutableLiveData<String>()
    val darkMode: LiveData<String> = _darkMode

    private val _poisAmount = MutableLiveData<String>()
    val poisAmount: LiveData<String> = _poisAmount

    private val _poisRating = MutableLiveData<Int>()
    val poisRating: LiveData<Int> = _poisRating

    private val _arqStatus = MutableLiveData<Boolean>()
    val arqStatus: LiveData<Boolean> = _arqStatus

    private val _cultStatus = MutableLiveData<Boolean>()
    val cultStatus: LiveData<Boolean> = _cultStatus

    private val _industStatus = MutableLiveData<Boolean>()
    val industStatus: LiveData<Boolean> = _industStatus

    private val _natStatus = MutableLiveData<Boolean>()
    val natStatus: LiveData<Boolean> = _natStatus

    private val _relStatus = MutableLiveData<Boolean>()
    val relStatus: LiveData<Boolean> = _relStatus

    private val _otherStatus = MutableLiveData<Boolean>()
    val otherStatus: LiveData<Boolean> = _otherStatus

    private val _poisDistance = MutableLiveData<Float>()
    val poisDistance: LiveData<Float> = _poisDistance

    fun showToast(context: Context, message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun setSwitch(isSystemInDarkMode: Boolean) {
        when(sharedPreferences.getString("dark_mode", "none")) {
            "enabled" -> {
                _switchChecked.value = true
                _darkMode.value = "enabled"
            }
            "disabled" -> {
                _switchChecked.value = false
                _darkMode.value = "disabled"
            }
            else -> _switchChecked.value = isSystemInDarkMode
        }
        setPreferences()
    }

    private fun setPreferences(){
        _poisAmount.value = sharedPreferences.getString("pois_amount", "5")
        _poisRating.value = sharedPreferences.getInt("pois_rating", -1)
        _arqStatus.value = sharedPreferences.getBoolean("arq_status", false)
        _cultStatus.value = sharedPreferences.getBoolean("cult_status", false)
        _industStatus.value = sharedPreferences.getBoolean("indust_status", false)
        _natStatus.value = sharedPreferences.getBoolean("nat_status", false)
        _relStatus.value = sharedPreferences.getBoolean("rel_status", false)
        _otherStatus.value = sharedPreferences.getBoolean("other_status", false)
        _poisDistance.value = sharedPreferences.getFloat("pois_distance", 12f)
    }

    fun onBackPressed(navigationController: NavHostController) {
        navigationController.navigate(Routes.ProfileScreen.route) {
            popUpTo(Routes.SettingsScreen.route) { inclusive = true }
        }
    }

    fun onSwitchChange(isSwitchChecked: Boolean) {
        _switchChecked.value = isSwitchChecked
        if (switchChecked.value == true) {
            _darkMode.value = "enabled"
        } else {
            _darkMode.value = "disabled"
        }
        editor.putString("dark_mode", darkMode.value)
        editor.apply()
    }

    fun onAmountChange(amount: String) {
        val regex = "[0-9]*".toRegex()
        if (amount != "") {
            if (regex.matches(amount)) {
                if (amount.toInt() < 1) {
                    _poisAmount.value = "1"
                } else if (amount.toInt() > 100) {
                    _poisAmount.value = "100"
                } else {
                    _poisAmount.value = amount
                }
            } else {
                _poisAmount.value = regex.find(amount)?.value
            }
        } else {
            _poisAmount.value = ""
        }
    }

    fun onRatingChange(rating: Int) {
        _poisRating.value = rating
    }

    fun onTypeChange(index: Int, status: Boolean) {
        when (index) {
            0 -> _arqStatus.value = status
            1 -> _cultStatus.value = status
            2 -> _industStatus.value = status
            3 -> _natStatus.value = status
            4 -> _relStatus.value = status
            5 -> _otherStatus.value = status
        }
    }

    fun onDistanceChange(distance: Float) {
        _poisDistance.value = distance
    }

    fun savePreferences(context: Context){
        editor.putString("pois_amount", poisAmount.value)
        editor.putInt("pois_rating", poisRating.value!!)
        editor.putBoolean("arq_status", arqStatus.value!!)
        editor.putBoolean("cult_status", cultStatus.value!!)
        editor.putBoolean("indust_status", industStatus.value!!)
        editor.putBoolean("nat_status", natStatus.value!!)
        editor.putBoolean("rel_status", relStatus.value!!)
        editor.putBoolean("other_status", otherStatus.value!!)
        editor.putFloat("pois_distance", poisDistance.value!!)
        editor.apply()
        showToast(context, R.string.preferences_saved)
    }
}