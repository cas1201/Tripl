package tfg.sal.tripl.ui.signup.ui

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import tfg.sal.tripl.model.Routes
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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun onBackPressed(navigationController: NavHostController) {
        navigationController.popBackStack()
        navigationController.popBackStack()
        navigationController.navigate(Routes.LoginScreen.route)
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

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPassword(password: String): Boolean =
        password.length >= 8 &&
                password.contains("[A-Z]".toRegex()) &&
                password.contains("[a-z]".toRegex()) &&
                password.contains("[0-9]".toRegex())

    private fun passwordMatch(password: String, passwordRepeat: String): Boolean =
        password == passwordRepeat

    fun onSignUpSelected(email: String, password: String, navigationController: NavHostController) {
        //_isLoading.value = true
        //firebaseAuth.createUserWithEmailAndPassword(email, password)
        //.addOnCompleteListener {
        //if (it.isSuccessful) {
        //_isLoading.value = false
        navigationController.popBackStack()
        navigationController.popBackStack()
        navigationController.navigate(Routes.LoginScreen.route)
        /*} else {
            //_isLoading.value = false
        }
    }*/

    }
}