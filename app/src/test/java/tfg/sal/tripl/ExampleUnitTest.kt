package tfg.sal.tripl

import com.google.firebase.auth.FirebaseAuth
import org.junit.Test

// Modify the following line to use hilt injections
class ExampleUnitTest {
    // Create a test to check the firebase authentication
    @Test
    fun firebaseAuthTest() {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword("", "")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Authentication success")
                } else {
                    println("Authentication failed")
                }
            }
    }
}