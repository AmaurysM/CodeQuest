package com.amaurysdm.codequest.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object LevelController {

    private var currentLevel by mutableIntStateOf(0)

    private val _levels = MutableStateFlow<List<Level>>(emptyList())
    val levels: StateFlow<List<Level>> = _levels

    init {
        val initialLevels = listOf(
            Level("Level 1", "rrdrd", false),
            Level("Level 2", "ddrrdddllu", false),
            Level("Level 3", "ddddddrrrruuulld", false),
            Level("Level 4", "rrddrrddrrddrrddrrddrrdd/", false),
            Level("Level 4", "ddrrdddldldld/", false)

        )

        _levels.value = initialLevels.map { level ->

            level.copy(
                isCompleted = FireBaseController.completedLevels.any { it.route == level.route }
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

    /*fun setCompletedLevels(completedLevels: List<Level>) {
        levels.forEach { level ->
            level.isCompleted = completedLevels.any { it.name == level.name }
        }
    }*/

}