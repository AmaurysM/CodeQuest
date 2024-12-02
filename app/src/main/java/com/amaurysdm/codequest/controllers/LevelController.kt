package com.amaurysdm.codequest.controllers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.amaurysdm.codequest.model.Level
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Controller for the levels, it manages the levels and the current level
// It also manages the completed levels
object LevelController {

    private var currentLevel by mutableIntStateOf(0)

    private val _levels = MutableStateFlow<List<Level>>(emptyList())
    val levels: StateFlow<List<Level>> = _levels

    // Don't like how I manged this here.
    // Next time ill put the phat in firebase and manage it from there
    // the level name is never used.
    init {
        val initialLevels = listOf( // I can make as many levels as I want.
            Level("Level 1", "rrdrd", false),
            Level("Level 2", "ddrrdddllu", false),
            Level("Level 3", "ddddddrrrruuulld", false),
            Level("Level 4", "rdrdrdrdrdrd/", false),
            Level("Level 5", "ddrrdddldldld/", false),
            Level("Level 6", "urururrrrddldldld/", false)
            // Its pretty easy to create a level. I made it so the route is parsed
            // and creates a level from it. It would still work even if there are
            // random characters in the route.
        )

        _levels.value = initialLevels.map { level ->

            level.copy(
                completed = FireBaseController.completedLevels.any { it.route == level.route }
            )
        }
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

}