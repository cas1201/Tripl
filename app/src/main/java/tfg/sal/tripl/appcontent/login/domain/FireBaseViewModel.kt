package tfg.sal.tripl.appcontent.login.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tfg.sal.tripl.appcontent.login.data.network.FireBaseAuthRepository
import tfg.sal.tripl.appcontent.login.data.network.FireBaseAuthResource
import javax.inject.Inject

@HiltViewModel
class FireBaseViewModel @Inject constructor(private val repository: FireBaseAuthRepository) :
    ViewModel() {

    private val _loginFlow = MutableStateFlow<FireBaseAuthResource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<FireBaseAuthResource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableStateFlow<FireBaseAuthResource<FirebaseUser>?>(null)
    val signupFlow: StateFlow<FireBaseAuthResource<FirebaseUser>?> = _signupFlow

    private val _recoverPasswordFlow = MutableStateFlow<FireBaseAuthResource<Void?>?>(null)
    val recoverPasswordFlow: StateFlow<FireBaseAuthResource<Void?>?> = _recoverPasswordFlow

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if (repository.currentUser != null) {
            _loginFlow.value = FireBaseAuthResource.Success(repository.currentUser!!)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginFlow.value = FireBaseAuthResource.Loading
            val result = repository.login(email, password)
            _loginFlow.value = result
        }
    }

    fun signup(name: String, email: String, password: String) {
        viewModelScope.launch {
            _signupFlow.value = FireBaseAuthResource.Loading
            val result = repository.signup(name, email, password)
            _signupFlow.value = result
        }
    }

    fun changePassword(email: String) {
        viewModelScope.launch {
            _recoverPasswordFlow.value = FireBaseAuthResource.Loading
            val result = repository.changePassword(email)
            _recoverPasswordFlow.value = result
        }
    }

    fun logout() {
        repository.logout()
        _loginFlow.value = null
        _signupFlow.value = null
    }
}