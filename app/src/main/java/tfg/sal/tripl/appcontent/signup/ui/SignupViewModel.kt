package tfg.sal.tripl.appcontent.signup.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import tfg.sal.tripl.core.Routes
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _surname = MutableLiveData<String>()
    val surname: LiveData<String> = _surname

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _passwordRepeat = MutableLiveData<String>()
    val passwordRepeat: LiveData<String> = _passwordRepeat

    private val _passwordVisible = MutableLiveData<Boolean>()
    val passwordVisible: LiveData<Boolean> = _passwordVisible

    private val _passwordRepeatVisible = MutableLiveData<Boolean>()
    val passwordRepeatVisible: LiveData<Boolean> = _passwordRepeatVisible

    private val _signupEnable = MutableLiveData<Boolean>()
    val signupEnable: LiveData<Boolean> = _signupEnable

    private val _signUpPressed = MutableLiveData<Boolean>()
    val signUpPressed: LiveData<Boolean> = _signUpPressed

    fun showToast(context: Context, message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        _signUpPressed.value = false
    }

    fun onBackPressed(navigationController: NavHostController) {
        clearTextFields()
        navigationController.navigate(Routes.LoginScreen.route) {
            popUpTo(Routes.LoginScreen.route) { inclusive = true }
        }
    }

    fun onPasswordVisibleChange(passwordVisible: Boolean) {
        _passwordVisible.value = !passwordVisible
    }

    fun onPasswordRepeatVisibleChange(passwordVisible: Boolean) {
        _passwordRepeatVisible.value = !passwordVisible
    }

    fun onSignUpChange(
        name: String,
        surname: String,
        email: String,
        password: String,
        passwordRepeat: String
    ) {
        _name.value = name
        _surname.value = surname
        _email.value = email
        _password.value = password
        _passwordRepeat.value = passwordRepeat
        _signupEnable.value = isValidEmail(email) &&
                isValidPassword(password) &&
                passwordMatch(password, passwordRepeat)
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

    private fun passwordMatch(password: String, passwordRepeat: String): Boolean =
        password == passwordRepeat

    fun onSignUpSelected(navigationController: NavHostController) {
        clearTextFields()
        _signUpPressed.value = true
        navigationController.navigate(Routes.LoginScreen.route) {
            popUpTo(Routes.LoginScreen.route) { inclusive = true }
        }
    }

    private fun clearTextFields() {
        _name.value = ""
        _surname.value = ""
        _email.value = ""
        _password.value = ""
        _passwordRepeat.value = ""
        _passwordVisible.value = false
        _passwordRepeatVisible.value = false
    }
}