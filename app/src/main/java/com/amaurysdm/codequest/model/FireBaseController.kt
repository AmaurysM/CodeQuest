package com.amaurysdm.codequest.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.navigation.Screens
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class LevelsCompletedData(var userId: String, var levelsCompleted: Array<Level>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LevelsCompletedData

        if (userId != other.userId) return false
        if (!levelsCompleted.contentEquals(other.levelsCompleted)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + levelsCompleted.contentHashCode()
        return result
    }
}

object FireBaseController {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var database: FirebaseFirestore = Firebase.firestore

    var loginJob = CoroutineScope(Dispatchers.IO + SupervisorJob())
    var registerJob = CoroutineScope(Dispatchers.IO + SupervisorJob())


    fun login(loginData: LoginData, onLogin: () -> Unit) {
        loginJob.launch {
            auth.signInWithEmailAndPassword(loginData.email, loginData.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        onLogin()
                    }
                }

        }
    }

    fun register(registerData: RegisterData, onRegister: () -> Unit): Boolean {
        var completed by mutableStateOf(false)
        registerJob.launch {
            auth.createUserWithEmailAndPassword(registerData.email, registerData.password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        completed = true
                        onRegister()
                        saveUserData(registerData)

                    }
                }

        }
        return completed
    }

    private fun saveUserData(registerData: RegisterData) {
        val user = User(
            userId = auth.currentUser?.uid ?: "",
            username = registerData.username,
            email = registerData.password
        )
        val usersRef = database.collection("user").document(user.userId)
        usersRef.set(user)
            .addOnCompleteListener {
                Log.d("FireBaseController", "User data saved successfully")

            }.addOnFailureListener {
                Log.e("FireBaseController", "Error saving user data: ${it.message}")
            }

    }

    fun saveLevels(){
        val userId = auth.currentUser?.uid ?: ""
        val usersRef = database.collection("completedLevels").document(userId)

        val newLevels = LevelsCompletedData(
            userId = auth.currentUser?.uid ?: "",
            levelsCompleted = LevelController.getAllLevels().filter { it.isCompleted }.toTypedArray()
        )

        usersRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                //val existingLevels = documentSnapshot.get("levelsCompleted") as? List<String> ?: emptyList()
                /*val existingLevels = (documentSnapshot.get("levelsCompleted") as? Array<String>)?.toMutableList() ?: mutableListOf()


                    // (existingLevels?.levelsCompleted?.plus(newLevels.levelsCompleted)?.toSet() ?: newLevels.levelsCompleted)
                val updatedLevels = existingLevels.plus(newLevels.levelsCompleted).toSet()

                usersRef.update("levelsCompleted", updatedLevels)
                    .addOnSuccessListener {
                        Log.d("FireBaseController", "User levelsCompleted updated successfully")
                    }
                    .addOnFailureListener {
                        Log.e("FireBaseController", "Error updating user levelsCompleted data: ${it.message}")

                    }*/

            } else {

                usersRef.set(newLevels).addOnSuccessListener {
                    Log.d("FireBaseController", "User levelsCompleted saved successfully")
                }.addOnFailureListener {
                    Log.e("FireBaseController", "Error saving user levelsCompleted data: ${it.message}")
                }
            }
        }.addOnFailureListener {
            Log.e("FireBaseController", "Error getting user levelsCompleted data: ${it.message}")
        }
    }



    suspend fun beginningDestination(whenLoggedIn: () -> Unit, whenNotLoggedIn: () -> Unit) {
        var isLoggedIn = false

        val checkLoginJob = GlobalScope.launch {
            isLoggedIn = auth.currentUser != null
        }

        delay(3000)

        checkLoginJob.join()

        if (isLoggedIn) {
            whenLoggedIn()

        } else {
            whenNotLoggedIn()
        }
    }
}