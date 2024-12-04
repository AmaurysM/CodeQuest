package com.amaurysdm.codequest.controllers.room

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.amaurysdm.codequest.model.Level
import com.amaurysdm.codequest.model.ParentChild
import com.amaurysdm.codequest.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object RoomController {
    lateinit var db: AppDatabase
    lateinit var userDao: UserDao
    lateinit var levelDao: LevelDao
    lateinit var parentChildDao: ParentChildDao

    val viewModelScope = CoroutineScope(Dispatchers.IO)

    private val _completedLevels = MutableStateFlow<List<Level>>(emptyList())
    val completedLevels: StateFlow<List<Level>> get() = _completedLevels

    var currentUser = User()

    private var loginJob: Job? = Job()
    private var registerJob: Job = Job()
    private var logoutJob: Job = Job()


    fun initializeRoom(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).build()
        userDao = db.userDao()
        levelDao = db.levelDao()
        parentChildDao = db.parentChildDao()
    }

    suspend fun getUserData(userId: Int) {
        currentUser = userDao.getUserById(userId) ?: User()
        _completedLevels.value =
            levelDao.getLevelsCompletedByUser(RoomController.currentUser.userId ?: 0)
    }

    suspend fun saveUserData(user: User) {
        userDao.insertUser(user)
    }

    suspend fun saveLevel(level: Level) {
        levelDao.insertLevel(level)
    }

    suspend fun getUser(userId: Int): User? {
        return userDao.getUserById(userId)
    }

    suspend fun getLevelByRoute(route: String): Level? {
        return levelDao.getLevelByRoute(route)
    }

    suspend fun getCompletedLevels(): List<Level> {
        return levelDao.getLevelsCompletedByUser(RoomController.currentUser.userId ?: 0)
    }

    suspend fun getLevelsCompletedByUser(userId: Int): List<Level> {
        return levelDao.getLevelsCompletedByUser(userId)
    }

    suspend fun login(email: String, password: String) {
        loginJob.run {
            currentUser = userDao.getUserByEmailAndPassword(email, password) ?: User()
            currentUser.loggedIn = true
            userDao.insertUser(currentUser)
        }
    }

    fun cancelLogin() {
        loginJob?.cancel()
    }

    suspend fun register(
        username: String = "",
        email: String = "",
        password: String = "",
        isAParent: Boolean = false,
    ) {
        return registerJob.run {
            userDao.insertUser(
                User(
                    username = username,
                    email = email,
                    password = password,
                    isAParent = isAParent
                )
            )

        }
    }

    fun cancelRegister() {
        registerJob.cancel()
    }

    suspend fun getChildren(parent: Int): List<User> {
        val children = parentChildDao.getChildren(parent)
        Log.e("Children", children.toString())
        val list = children.map {
            userDao.getUserById(it.childId)
        }

        return list.mapNotNull {
            it
        }
    }

    suspend fun removeChild(parent: Int, child: Int) {
        parentChildDao.removeChild(parent, child)
    }

    suspend fun addChild(parent: Int, child: Int) {
        parentChildDao.insertParentChild(ParentChild(parent, child))
    }

    suspend fun findLoggedInUser(): User? {
        return userDao.findLoggedInUser()
    }

    suspend fun logout() {
        logoutJob.run {
            currentUser.loggedIn = false
            userDao.insertUser(currentUser)

        }

    }

    suspend fun getUserByMail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    fun cancelLogout() {
        logoutJob.cancel()
    }


}