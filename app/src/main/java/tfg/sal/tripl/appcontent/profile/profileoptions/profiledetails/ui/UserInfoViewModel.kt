package tfg.sal.tripl.appcontent.profile.profileoptions.profiledetails.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.signup.data.User
import tfg.sal.tripl.core.Routes
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebase: FirebaseAuth
) : ViewModel() {

    private val _userData = MutableLiveData<User>()
    val userData: MutableLiveData<User> = _userData

    fun onBackPressed(navigationController: NavHostController) {
        navigationController.navigate(Routes.ProfileScreen.route) {
            popUpTo(Routes.ProfileScreen.route) { inclusive = true }
        }
    }

    private fun showToast(context: Context, message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getUserData(context: Context) {
        firestore.collection(firebase.currentUser!!.uid)
            .document("userData")
            .get()
            .addOnSuccessListener {
                _userData.value = it.toObject(User::class.java)
            }.addOnFailureListener {
                showToast(context, R.string.get_user_error)
            }
    }

    fun getEmail(context: Context): String {
        val email = firebase.currentUser!!.email!!
        showToast(context, R.string.change_password_email_sent)
        return email
    }
}