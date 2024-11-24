package com.amaurysdm.codequest.ui.level

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.amaurysdm.codequest.model.GameState
import com.amaurysdm.codequest.model.levels.LevelController

class LevelViewmodel: ViewModel() {

    var currentState = mutableStateOf(Pair(0, 0))


    fun getLevel(): GameState {
        /*var state = createLevelFromText(LevelController.getLevel())
        currentState = mutableStateOf(state.playerPosition)*/
        return createLevelFromText(LevelController.getLevel())
    }

    private fun createLevelFromText(text: String): GameState {
        val lines = text.toCharArray()
        val startingPosition = Pair(2, 5)

        var currentPosition = startingPosition
        val path = mutableListOf(currentPosition)
        lines.forEachIndexed { index, value ->
            currentPosition = when (value) {
                'u' -> Pair(currentPosition.first, currentPosition.second - 1)
                'd' -> Pair(currentPosition.first, currentPosition.second + 1)
                'r' -> Pair(currentPosition.first + 1, currentPosition.second)
                'l' -> Pair(currentPosition.first - 1, currentPosition.second)
                else -> {
                    Pair(currentPosition.first, currentPosition.second)
                }
            }
            path.add(currentPosition)
        }
        currentState = mutableStateOf(startingPosition)
        return GameState(startingPosition, path)
    }

    fun movePlayer(position: Pair<Int, Int> , direction: Char) {
        when (direction) {
            'u' -> position.second - 1 //Pair(currentState.value.first, currentState.value.second - 1)
            'd' -> position.second + 1 //Pair(currentState.value.first, currentState.value.second + 1)
            'r' -> position.first + 1 //Pair(currentState.value.first + 1, currentState.value.second)
            'l' -> position.first - 1 //Pair(currentState.value.first - 1, currentState.value.second)

            else -> {
                Pair(currentState.value.first, currentState.value.second)
            }
        }/**/
        //Log.d("LevelController", "movePlayer: ${currentState.value}")
    }
}

