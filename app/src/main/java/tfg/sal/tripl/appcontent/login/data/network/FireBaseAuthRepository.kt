package tfg.sal.tripl.appcontent.login.data.network

import com.google.firebase.auth.FirebaseUser

interface FireBaseAuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): FireBaseAuthResource<FirebaseUser>
    suspend fun signup(
        name: String,
        email: String,
        password: String
    ): FireBaseAuthResource<FirebaseUser>

    suspend fun recoverPassword(email: String): FireBaseAuthResource<Void?>
    fun logout()
}