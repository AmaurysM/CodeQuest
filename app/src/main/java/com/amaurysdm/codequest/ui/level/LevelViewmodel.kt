package com.amaurysdm.codequest.ui.level

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.R
import com.amaurysdm.codequest.model.GameState
import com.amaurysdm.codequest.model.LevelController
import com.amaurysdm.codequest.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class LevelViewmodel : ViewModel() {

    private val level = LevelController.getLevel()
    private var levelText = level.route
    private var currentMovingDirectionIndex by mutableIntStateOf(0)

    val uniqueDirection = levelText.toSet()

    var currentState = createLevelFromText()
    var positionX by mutableIntStateOf(currentState.playerPosition.first)
    var positionY by mutableIntStateOf(currentState.playerPosition.second)
    var isAnimating by mutableStateOf(false)

    fun countTurnsInLevel(): Int {
        if (levelText.isEmpty()) {
            return 1
        }

        var changes = 1
        for (i in 1 until levelText.length) {
            if (levelText[i] != levelText[i - 1]) {
                changes++
            }
        }
        return changes
    }

    private fun createLevelFromText(): GameState {
        val startingPosition = Pair(2, 5)

        var currentPosition = startingPosition
        val path = mutableListOf(currentPosition)
        levelText.forEach { value ->
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
        return GameState(startingPosition, path)
    }

    private fun moveDirection(
        chars: Char,
        repeatThis: Pair<Pair<Char, Char>, Int> = Pair(Pair('d', 'r'), 5)
    ): List<Pair<Int, Int>> {
        val moveDirection = mutableListOf<Pair<Int, Int>>()

        when (chars) {
            'u' -> moveDirection.add(Pair(0, -1))
            'd' -> moveDirection.add(Pair(0, 1))
            'r' -> moveDirection.add(Pair(1, 0))
            'l' -> moveDirection.add(Pair(-1, 0))
            /*'/' -> Pair((moveDirection(repeatThis.first.first).first * repeatThis.second)
                    + (moveDirection(repeatThis.first.second).first * repeatThis.second)
                , (moveDirection(repeatThis.first.first).second * repeatThis.second) +
                        (moveDirection(repeatThis.first.second).second * repeatThis.second))*/
            else -> Pair(0, 0)
        } // That point is in the path keep going intill you find it.
        return moveDirection
    }


    suspend fun movePlayer(chars: List<Char>, navController: NavHostController) {

        fun isValidMove(x: Int, y: Int): Boolean {
            return currentState.path
                .contains(
                    Pair(
                        x + moveDirection(chars[currentMovingDirectionIndex]).first().first,
                        y + moveDirection(chars[currentMovingDirectionIndex]).first().second
                    )
                )
        }

        suspend fun goToLevelSelect(navController: NavHostController) {
            withContext(Dispatchers.Main) {
                navController.navigate(Screens.GameChild.LevelSelect.route)
            }
        }

        while (isAnimating) {
            delay(150)
            val isValid = isValidMove(positionX, positionY)

            if (positionX == currentState.path.last().first
                && positionY == currentState.path.last().second
            ) {
                goToLevelSelect(navController)
                level.isCompleted = true
                isAnimating = false

            } else if (isValid) {
                val movement = moveDirection(chars[currentMovingDirectionIndex])
                positionX += movement.first().first
                positionY += movement.first().second
            } else {
                if (currentMovingDirectionIndex < chars.size) {
                    currentMovingDirectionIndex++
                }
                if (currentMovingDirectionIndex == chars.size ||
                    chars[currentMovingDirectionIndex] == ' '
                ) {

                    positionX = currentState.path.first().first
                    positionY = currentState.path.first().second
                    currentMovingDirectionIndex = 0
                    isAnimating = false

                }
            }
        }

        isAnimating = false
    }

    fun getImage(it: Char): Int {
        return when (it) {
            'u' -> R.drawable.baseline_arrow_upward_24
            'd' -> R.drawable.baseline_arrow_downward_24
            'r' -> R.drawable.baseline_arrow_forward_24
            'l' -> R.drawable.baseline_arrow_back_24
            '/' -> R.drawable.baseline_home_24
            else -> {
                R.drawable.baseline_settings_24
            }
        }
    }
}

