package com.amaurysdm.codequest.model

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class LevelsCompletedData(var userId: String, var levelsCompleted: List<Level>)

object FireBaseController {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var loginJob = CoroutineScope(Dispatchers.IO + SupervisorJob())
    var registerJob = CoroutineScope(Dispatchers.IO + SupervisorJob())
    var coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    var completedLevels = mutableListOf<Level>()
    var currentUser = User()

    init {
        val user = auth.currentUser
        if (user != null) {
            getUserData()
        }

    }

    fun getUserData(){
        val db = Firebase.firestore
        val userId = auth.currentUser?.uid ?: ""

        val levelsRef = db.collection("completedLevels").document(userId)
        levelsRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val levels =
                    document["levelsCompleted"] as? List<Map<String, Any>> ?: emptyList()
                completedLevels = levels.map {
                    Level(
                        it["name"] as String, it["route"] as String, it["completed"] as Boolean
                    )

                }.toMutableList()

            }
        }
        val usersRef = db.collection("user").document(userId)
        usersRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                currentUser = User(
                    userId = document["userId"] as String,
                    username = document["username"] as String,
                    email = document["email"] as String,
                    children = document["children"] as? List<String> ?: emptyList(),
                    parent = document["parent"] as? String ?: "",
                    isAParent = document["aparent"] as? Boolean ?: false
                )
            }
        }

    }

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

    fun register(registerData: RegisterData, onRegister: () -> Unit) {
        registerJob.launch {
            auth.createUserWithEmailAndPassword(registerData.email, registerData.password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        onRegister()
                        saveUserData(registerData)

                    }
                }

        }
    }

    private fun saveUserData(registerData: RegisterData) {
        val database = Firebase.firestore
        val user = User(
            userId = auth.currentUser?.uid ?: "",
            username = registerData.username,
            email = registerData.password,
            children = emptyList(),
            parent = "",
            isAParent = registerData.areYouAParent
        )
        val usersRef = database.collection("user").document(user.userId)
        usersRef.set(user)

    }

    fun saveLevels() {
        val db = Firebase.firestore
        val userId = auth.currentUser?.uid ?: ""

        val usersRef = db.collection("completedLevels").document(userId)

        val newLevels = LevelController.getAllLevels().filter { it.isCompleted }
        usersRef.get()
            .addOnSuccessListener { document ->

                if (document.exists()) {
                    val levels =
                        document["levelsCompleted"] as? List<Map<String, Any>> ?: emptyList()
                    val existingLevels = levels.map {
                        Level(
                            it["name"] as String, it["route"] as String, it["completed"] as Boolean
                        )
                    }

                    val updatedLevels = existingLevels.plus(newLevels).toSet().toList()
                    usersRef.set(LevelsCompletedData(userId, updatedLevels))

                } else {
                    usersRef.set(LevelsCompletedData(userId, newLevels))
                }
            }

    }

    fun getUserCurrentUserData(): User {
        val db = Firebase.firestore
        val userId = auth.currentUser?.uid ?: ""
        val usersRef = db.collection("user").document(userId)
        var user = User()
        usersRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                user = User(
                    userId = document["userId"] as String,
                    username = document["username"] as String,
                    email = document["email"] as String,
                    children = document["children"] as? List<String> ?: emptyList()
                )
            }
        }
        return user
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

    suspend fun getUser(children: String): User {
        val db = Firebase.firestore
        val usersRef = db.collection("user").document(children)
        var user = User()
        coroutineScope.launch {
        usersRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                user = User(
                    userId = document["userId"] as String,
                    username = document["username"] as String,
                    email = document["email"] as String,
                    children = document["children"] as? List<String> ?: emptyList()
                )
            }
        }}.join()
        return user
    }
}