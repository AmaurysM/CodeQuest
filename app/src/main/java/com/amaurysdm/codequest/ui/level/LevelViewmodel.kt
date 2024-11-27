package com.amaurysdm.codequest.ui.level

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.model.Directions
import com.amaurysdm.codequest.model.GameState
import com.amaurysdm.codequest.model.LevelController
import com.amaurysdm.codequest.model.movableDirections
import com.amaurysdm.codequest.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class TopBarItem(
    var direction: MutableState<Directions> = mutableStateOf(Directions.Nothing),
    var isVisible: MutableState<Boolean> = mutableStateOf(false),
    var children: MutableList<TopBarItem> = mutableStateListOf()
)

class LevelViewmodel : ViewModel() {

    var gameState by mutableStateOf(GameState(Pair(0, 0), mutableListOf<Pair<Int, Int>>()))
    private var uniqueDirectionInGameState = setOf<Directions>()
    var draggingItem by mutableStateOf<TopBarItem>(TopBarItem())
    var clickedItem by mutableStateOf<TopBarItem>(TopBarItem())
    var isAnimating by mutableStateOf(false)
    var screenPosition by mutableStateOf(Offset.Zero)

    private var currentMove by mutableIntStateOf(0)


    var topBarItems = mutableStateListOf<TopBarItem>()
    var bottomBarItems = mutableListOf<Directions>()


    init {
        gameState = GameState(Pair(0, 0), createPathCoordinates())
        uniqueDirectionInGameState =
            movableDirections.filter { createDirectionPath().toSet().contains(it) }.toSet()
        topBarItems =
            mutableStateListOf<TopBarItem>().apply { repeat(numberOfTurns()) { add(TopBarItem()) } }
        bottomBarItems = uniqueDirectionInGameState.toMutableList()
    }


    // Creates a list of directions based on the current Level
    private fun createDirectionPath(): MutableList<Directions> {
        val path = mutableListOf<Directions>()
        LevelController.getLevel().route.forEach { character ->
            path.add(movableDirections.find { it.direction == character } ?: Directions.Nothing)
        }
        return path
    }

    // Creates a list of coordinates based on the current Level
    private fun createPathCoordinates(): MutableList<Pair<Int, Int>> {
        val movableDirections = createDirectionPath()
        var currentPosition = Pair(0, 0)
        val pathCoordinates = mutableListOf<Pair<Int, Int>>(currentPosition)

        movableDirections.forEach { direction ->

            currentPosition = Pair(
                currentPosition.first + direction.movement.first,
                currentPosition.second + direction.movement.second
            )
            pathCoordinates.add(currentPosition)
        }
        return pathCoordinates
    }

    private fun numberOfTurns(): Int {
        if (LevelController.getLevel().route.isEmpty()) {
            return 0
        }

        var changes = 0
        for (i in 1 until LevelController.getLevel().route.length) {
            if (LevelController.getLevel().route[i] != LevelController.getLevel().route[i - 1]) {
                changes++
            }
        }
        return changes
    }

    private fun usableTopBarItems(): MutableList<TopBarItem> {
        val usableTopBarItems =
            topBarItems.filter { it.direction.value != Directions.Nothing }.toMutableList()

        var i = 0
        while (i < usableTopBarItems.size) {
            val currentItem = usableTopBarItems[i]

            if (currentItem.direction.value == Directions.Repeat) {

                val usableChildren =
                    currentItem.children.filter { it.direction.value != Directions.Nothing }
                usableTopBarItems.removeAt(i)

                if (usableChildren.isEmpty()) {
                    break
                }

                for (j in usableChildren.size - 1 downTo 0) {
                    usableTopBarItems.add(i, usableChildren[j])
                }

            } else {
                i++
            }
        }
        Log.w("topBarItems", "$usableTopBarItems")

        return usableTopBarItems
    }

    private fun validMove(move: Directions): Boolean {
        return gameState.path.map { it }
            .contains(
                Pair(
                    move.movement.first + gameState.playerPosition.first,
                    move.movement.second + gameState.playerPosition.second
                )
            )
    }

    suspend fun playButton(navController: NavHostController) {
        val chosenDirection = usableTopBarItems()

        isAnimating = true
        while (true) {
            delay(150)
            if (chosenDirection.isEmpty()) break



            if (currentMove == chosenDirection.size) {
                break
            }

            if (validMove(chosenDirection[currentMove].direction.value)) {
                gameState = gameState.copy(
                    playerPosition = Pair(
                        first = gameState.playerPosition.first + chosenDirection[currentMove].direction.value.movement.first,
                        second = gameState.playerPosition.second + chosenDirection[currentMove].direction.value.movement.second
                    )
                )
            } else {
                currentMove++
                if (gameState.playerPosition == gameState.path.last()) {
                    withContext(Dispatchers.Main) {
                        launch {
                            navController.navigate(Screens.GameChild.LevelSelect.route)
                            LevelController.getLevel().isCompleted = true
                        }
                    }
                    //delay(300)
                    break
                }
            }
        }
        resetAnimation()
        resetPosition()
    }

    private fun resetAnimation() {
        isAnimating = false
        currentMove = 0
    }

    private fun resetPosition() {
        gameState = gameState.copy(playerPosition = createPathCoordinates().first())
        isAnimating = false
    }

}

