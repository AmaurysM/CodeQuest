package com.amaurysdm.codequest.model.levels


object LevelController {

    var currentLevel = 0

    private val levels = listOf<String>(
        "rrdrd",
        "ddrrddllu",
        "ddddddrrrruuulld"
    )

    fun getCertainLevel(level: Int): String {
        return levels[level]
    }

    fun getLevel(): String {
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