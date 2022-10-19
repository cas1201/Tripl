package tfg.sal.tripl.ui.recoverpassword.ui

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import tfg.sal.tripl.model.Routes
import javax.inject.Inject

@HiltViewModel
class RecoverPasswordViewModel @Inject constructor() : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _recoverPasswordEnable = MutableLiveData<Boolean>()
    val recoverPasswordEnable: LiveData<Boolean> = _recoverPasswordEnable

    fun onBackPressed(navigationController: NavHostController) {
        navigationController.popBackStack()
        navigationController.popBackStack()
        navigationController.navigate(Routes.LoginScreen.route)
    }

    fun onSignUpChange(email: String) {
        _email.value = email
        _recoverPasswordEnable.value = isValidEmail(email)
    }

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()


    fun onRecoverPasswordSelected(navigationController: NavHostController) {
        //_isLoading.value = true
        navigationController.popBackStack()
        navigationController.popBackStack()
        //_isLoading.value = false
        navigationController.navigate(Routes.LoginScreen.route)
    }
}