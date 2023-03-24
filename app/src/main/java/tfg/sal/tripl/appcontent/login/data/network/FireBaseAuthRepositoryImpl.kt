package tfg.sal.tripl.appcontent.login.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : FireBaseAuthRepository {
    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun login(
        email: String,
        password: String
    ): FireBaseAuthResource<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            FireBaseAuthResource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            FireBaseAuthResource.Error(e)
        }
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): FireBaseAuthResource<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            FireBaseAuthResource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            FireBaseAuthResource.Error(e)
        }
    }

    override suspend fun changePassword(email: String): FireBaseAuthResource<Void?> {
        return try {
            val result = auth.sendPasswordResetEmail(email).await()
            FireBaseAuthResource.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            FireBaseAuthResource.Error(e)
        }
    }

    override fun logout() {
        auth.signOut()
    }
}