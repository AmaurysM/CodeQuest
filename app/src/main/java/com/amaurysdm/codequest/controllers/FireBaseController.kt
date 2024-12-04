// NO LONGER USING THIS FILE.

/*
package com.amaurysdm.codequest.controllers

import com.amaurysdm.codequest.model.Level
import com.amaurysdm.codequest.model.LevelsCompletedData
import com.amaurysdm.codequest.model.LoginData
import com.amaurysdm.codequest.model.RegisterData
import com.amaurysdm.codequest.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

// Firebase Controller, controls the firebase database
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

    // gets the user data from firebase
    // gets the levels that the user has completed from firebase
    // But theres an error here. More like something I could have done better.
    // The instance of the firebase datastore that I have is not being updated live
    // do when I add a user you con only see the updated information after restarting the app.
    fun getUserData() {
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
                    parent = document["parent"] as? String ?: "",
                    isAParent = document["aparent"] as? Boolean ?: false
                )
            }
        }

    }

    // logs in the user to firebase
    // don't know if it would be better to have this in the loginViewmodel
    fun login(loginData: LoginData, onLogin: () -> Unit) {
        loginJob.launch {
            auth.signInWithEmailAndPassword(loginData.email, loginData.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        onLogin()
                    }
                    getUserData()
                }

        }
    }

    // registers the user to firebase
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

    // saves the user data to firebase
    fun saveUserData(registerData: RegisterData) {
        val database = Firebase.firestore
        val user = User(
            userId = auth.currentUser?.uid ?: "",
            username = registerData.username,
            email = registerData.email,
            parent = "",
            isAParent = registerData.areYouAParent
        )
        val usersRef = database.collection("user").document(user.userId)
        usersRef.set(user)

    }

    // saves the levels that the user has completed to firebase
    fun saveLevels() {
        val db = Firebase.firestore
        val userId = auth.currentUser?.uid ?: ""

        val usersRef = db.collection("completedLevels").document(userId)

        val newLevels = LevelController.getAllLevels().filter { it.completed }
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

    // gets the user data from firebase
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
                    parent = "",
                    isAParent = document["aparent"] as Boolean
                )
            }
        }
        return user
    }

    // checks if the user is logged in or not.
    // Navigates accordingly.
    suspend fun beginningDestination(whenLoggedIn: () -> Unit, whenNotLoggedIn: () -> Unit) {
        var isLoggedIn = false

        val checkLoginJob = coroutineScope.launch {
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

    // gets the user data from firebase
    suspend fun getUser(userID: String): User {

        val db = Firebase.firestore
        val userRef = db.collection("user").document(userID) // gets the document of the user
        return suspendCoroutine { continuation ->
            userRef.get().addOnSuccessListener { document ->
                if (document.exists()) { // if the document exists
                    val user = User( // creates a new user object from the data in the document
                        userId = document["userId"] as String,
                        username = document["username"] as String,
                        email = document["email"] as String,
                        isAParent = document["aparent"] as Boolean
                    )
                    continuation.resume(user) // resumes the coroutine with the user object
                } else {
                    continuation.resume(User())
                }
            }.addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
        }

    }

    // gets the user data from firebase using an email.
    // I'm using an email and password to create users so emails are all unique.
    suspend fun getUserFromEmail(email: String): User? {
        val db = Firebase.firestore
        val usersRef = db.collection("user").whereEqualTo("email", email)

        return try {
            usersRef.get().await().documents.firstOrNull()?.let { document ->
                User(
                    userId = document["userId"] as String,
                    username = document["username"] as String,
                    email = document["email"] as String,
                    isAParent = document["aparent"] as Boolean
                )
            }
        } catch (e: Exception) {
            null
        }

    }

    // gets the levels that the user has completed from firebase using the user id
    suspend fun getCompletedLevelsByUser(id: String): List<Level?> {
        val db = Firebase.firestore
        val usersRef = db.collection("completedLevels").document(id)

        return try {
            usersRef.get().await().let { document ->
                val levels =
                    document["levelsCompleted"] as? List<Map<String, Any>> ?: emptyList()

                levels.map {
                    Level(
                        it["name"] as String, it["route"] as String, it["completed"] as Boolean
                    )
                }
            }

        } catch (e: Exception) {
            emptyList()
        }

    }

}*/
