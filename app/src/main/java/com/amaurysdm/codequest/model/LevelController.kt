package com.amaurysdm.codequest.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

data class Level(val name: String, val route: String, var isCompleted: Boolean = false)
object LevelController {

    var currentLevel by mutableIntStateOf(0)

    private val levels = listOf<Level>(
        Level("Level 1", "rrdrd/", false),
        Level("Level 2", "ddrrddllu", false),
        Level("Level 3", "ddddddrrrruuulld", false),
        Level("Level 4", "rrddrrddrrddrrddrrddrrdd/", false)
    )

    fun getCertainLevel(level: Int): Level {
        return levels[level]
    }

    fun getLevel(): Level {
        return levels[currentLevel]
    }

    fun numberOfLevels(): Int {
        return levels.size
    }

    fun nextLevel() {
        currentLevel++
    }

    fun setLevel(level: Int) {
        currentLevel = level
    }

}