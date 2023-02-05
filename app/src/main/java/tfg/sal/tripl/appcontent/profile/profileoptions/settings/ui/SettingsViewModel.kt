package tfg.sal.tripl.appcontent.profile.profileoptions.settings.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.preference.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import tfg.sal.tripl.core.Routes
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val app: Application) : ViewModel() {

    private val sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(app.applicationContext)
    private val editor = sharedPreferences.edit()

    private val _switchChecked = MutableLiveData<Boolean>()
    val switchChecked: LiveData<Boolean> = _switchChecked

    private val _darkMode = MutableLiveData<String>()
    val darkMode: LiveData<String> = _darkMode

    private val _language = MutableLiveData<String>()
    val language: LiveData<String> = _language

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
    }

    fun onBackPressed(navigationController: NavHostController) {
        navigationController.navigate(Routes.ProfileScreen.route) {
            popUpTo(Routes.SettingsScreen.route) { inclusive = true }
        }
    }

    fun onLanguageChange(selectedLanguage: String) {
        val language =
            if (selectedLanguage == "Español" || selectedLanguage == "Spanish") "es" else "en"
        _language.value = selectedLanguage
        editor.putString("language", language)
        editor.apply()
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
}