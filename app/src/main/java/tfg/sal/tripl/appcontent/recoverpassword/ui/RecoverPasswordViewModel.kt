package tfg.sal.tripl.appcontent.recoverpassword.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import tfg.sal.tripl.core.Routes
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class RecoverPasswordViewModel @Inject constructor() : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _recoverPasswordEnable = MutableLiveData<Boolean>()
    val recoverPasswordEnable: LiveData<Boolean> = _recoverPasswordEnable

    fun onBackPressed(navigationController: NavHostController) {
        clearTextFields()
        navigationController.navigate(Routes.LoginScreen.route) {
            popUpTo(Routes.LoginScreen.route) { inclusive = true }
        }
    }

    fun onRecoverPasswordChange(email: String) {
        _email.value = email
        _recoverPasswordEnable.value = isValidEmail(email)
    }

    private fun isValidEmail(email: String): Boolean {
        var isValid = false
        val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }


    fun onRecoverPasswordSelected(navigationController: NavHostController) {
        clearTextFields()
        navigationController.navigate(Routes.LoginScreen.route) {
            popUpTo(Routes.LoginScreen.route) { inclusive = true }
        }
    }

    private fun clearTextFields() {
        _email.value = ""
    }
}