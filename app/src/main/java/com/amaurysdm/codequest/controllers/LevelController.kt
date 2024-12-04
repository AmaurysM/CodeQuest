package com.amaurysdm.codequest.controllers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.amaurysdm.codequest.controllers.room.RoomController
import com.amaurysdm.codequest.controllers.room.RoomController.viewModelScope
import com.amaurysdm.codequest.model.Level
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Controller for the levels, it manages the levels and the current level
// It also manages the completed levels
object LevelController {


    private var currentLevel by mutableIntStateOf(0)

    private val _levels = MutableStateFlow<List<Level>>(emptyList())
    val levels: StateFlow<List<Level>> = _levels

    // Initialize RoomController
    private val roomController = RoomController

    // Initialize the levels from Room database
    init {
        loadLevels()
    }

    fun loadLevels() {
        roomController.db.levelDao().let { dao ->
            viewModelScope.launch {

                val initialLevels = createInitialLevels()

                dao.getLevelsCompletedByUser(RoomController.currentUser.userId ?: 0).forEach {
                    initialLevels.find { level -> level.route == it.route }?.let { level ->
                        level.completed = true
                        level.id = it.id
                    }
                }

                _levels.value = initialLevels

            }
        }
    }

    private fun createInitialLevels(): List<Level> {
        return listOf(
            Level("Level 1", "rrdrd", false),
            Level("Level 2", "ddrrdddllu", false),
            Level("Level 3", "ddddddrrrruuulld", false),
            Level("Level 4", "dddddrrruuullllld", false),
            Level("Level 5", "rdrdrdrdrdrd/", false),
            Level("Level 6", "ddrrdddldldld/", false),
            Level("Level 7", "urururrrrddldldld/", false),
            Level("Level 8", "ddddrrrruulldddddrrrrruulldddd/", false)

        )
    }

    fun getCertainLevel(level: Int): Level {
        return _levels.value[level]
    }

    fun getLevel(): Level {
        return levels.value[currentLevel]
    }

    fun numberOfLevels(): Int {
        return levels.value.size
    }

    fun getAllLevels(): List<Level> {
        return levels.value
    }

    fun nextLevel() {
        currentLevel++
    }

    fun setLevel(level: Int) {
        currentLevel = level
    }

    fun markLevelCompleted(level: Level) {
        val updatedLevel =
            level.copy(completed = true, userId = RoomController.currentUser.userId ?: 0)
        viewModelScope.launch {
            roomController.saveLevel(updatedLevel)
            _levels.value = _levels.value.map {
                if (it.route == updatedLevel.route) updatedLevel else it
            }
        }
    }

    fun updateAndSaveCurrentLevel(b: Boolean) {
        levels.value[currentLevel].completed = b
        levels.value[currentLevel].userId = RoomController.currentUser.userId ?: 0
        viewModelScope.launch {
            roomController.saveLevel(levels.value[currentLevel])
        }
    }


}