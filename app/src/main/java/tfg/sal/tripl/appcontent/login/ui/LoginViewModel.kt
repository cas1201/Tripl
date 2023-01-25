package tfg.sal.tripl.appcontent.login.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import tfg.sal.tripl.core.Routes
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _passwordVisible = MutableLiveData<Boolean>()
    val passwordVisible: LiveData<Boolean> = _passwordVisible

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    fun onPasswordVisibleChange(passwordVisible: Boolean) {
        _passwordVisible.value = !passwordVisible
    }

    fun onLoginChange(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPassword(password)
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

    private fun isValidPassword(password: String): Boolean =
        password.length >= 8 &&
                password.contains("[A-Z]".toRegex()) &&
                password.contains("[a-z]".toRegex()) &&
                password.contains("[0-9]".toRegex())

    fun onLoginSelected(navigationController: NavHostController) {
        clearTextFields()
        navigationController.navigate(Routes.HomeScreen.route) {
            popUpTo(Routes.HomeScreen.route) { inclusive = true }
        }
    }

    fun onPasswordForget(navigationController: NavHostController) {
        navigationController.navigate(Routes.RecoverPasswordScreen.route)
    }

    fun onSignUpPressed(navigationController: NavHostController) {
        navigationController.navigate(Routes.SignUpScreen.route)
    }

    private fun clearTextFields() {
        _email.value = ""
        _password.value = ""
    }
}