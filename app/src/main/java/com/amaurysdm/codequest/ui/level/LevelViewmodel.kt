package com.amaurysdm.codequest.ui.level

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.R
import com.amaurysdm.codequest.model.Directions
import com.amaurysdm.codequest.model.FireBaseController
import com.amaurysdm.codequest.model.GameState
import com.amaurysdm.codequest.model.LevelController
import com.amaurysdm.codequest.model.movableDirections
import com.amaurysdm.codequest.navigation.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

data class TopBarItem(
    var direction: MutableState<Directions> = mutableStateOf(Directions.Nothing),
    var isVisible: MutableState<Boolean> = mutableStateOf(false),
    var children: MutableList<TopBarItem> = mutableStateListOf(),
    var repeater: MutableIntState = mutableIntStateOf(1)
)

class LevelViewmodel : ViewModel() {

    private var startLocation by mutableStateOf(Pair(2, 4))

    var gameState by mutableStateOf(GameState(startLocation, mutableListOf<Pair<Int, Int>>()))
    private var uniqueDirectionInGameState = setOf<Directions>()
    var draggingItem by mutableStateOf<TopBarItem>(TopBarItem())
    var clickedItem by mutableStateOf<TopBarItem>(TopBarItem())
    var isAnimating by mutableStateOf(false)
    var screenPosition by mutableStateOf(Offset.Zero)

    var currentMove by mutableIntStateOf(0)

    val animatableX = Animatable(gameState.playerPosition.first.toFloat())
    val animatableY = Animatable(gameState.playerPosition.second.toFloat())


    var topBarItems = mutableStateListOf<TopBarItem>()
    var bottomBarItems = mutableListOf<Directions>()

    /*val mMediaPlayer = MediaPlayer.create(coroutineContext, R.raw.speedsound)*/


    init {
        gameState = GameState(startLocation, createPathCoordinates())
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
        var currentPosition = gameState.playerPosition
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
            return 1
        }

        var changes = 1
        for (i in 1 until LevelController.getLevel().route.length) {
            if (LevelController.getLevel().route[i] != LevelController.getLevel().route[i - 1]) {
                changes++
            }
        }
        return changes
    }

    private fun locationOfTurnsAndFlag(points: List<Pair<Int, Int>>): List<Pair<Int, Int>> {

        if (points.size < 3) return emptyList()

        val turns = mutableListOf<Pair<Int, Int>>()

        for (i in 1 until points.size - 1) {

            val previous = points[i - 1]
            val current = points[i]
            val next = points[i + 1]

            val vector1 = Pair(current.first - previous.first, current.second - previous.second)
            val vector2 = Pair(next.first - current.first, next.second - current.second)

            if (vector1.first * vector2.second != vector1.second * vector2.first) {
                turns.add(current)
            }
            Log.e("LevelViewmodel", "turnsInLevel: $turns")

        }
        turns.add(points.last())
        return turns
    }

    fun usableTopBarItems(): MutableList<TopBarItem> {
        val usableTopBarItems =
            topBarItems.filter { it.direction.value != Directions.Nothing }.toMutableList()

        var i = 0
        while (i < usableTopBarItems.size) {
            val currentItem = usableTopBarItems[i]

            if (currentItem.direction.value == Directions.Repeat) {

                val usableChildren =
                    currentItem.children.filter { it.direction.value != Directions.Nothing }
                val repeats = currentItem.repeater?.intValue
                usableTopBarItems.removeAt(i)

                if (usableChildren.isEmpty()) {
                    break
                }

                repeat(repeats ?: 0) {
                    for (j in usableChildren.size - 1 downTo 0) {
                        usableTopBarItems.add(i, usableChildren[j])
                    }
                }

            } else {
                i++
            }
        }
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
        val turnsInLevel = locationOfTurnsAndFlag(gameState.path)
        val scope = CoroutineScope(coroutineContext)


        while (isAnimating) {
            delay(150)
            if (chosenDirection.isEmpty()) break

            if (currentMove == chosenDirection.size) {
                break
            }

            if (validMove(chosenDirection[currentMove].direction.value)) {


                scope.launch {
                    val mediaPlayer = MediaPlayer.create(navController.context, R.raw.speedsound)
                    mediaPlayer.start()
                    if (chosenDirection[currentMove].direction.value.movement.first == 0) {
                        animatableY.animateTo(
                            targetValue = turnsInLevel[currentMove].second.toFloat(),
                            animationSpec = tween(durationMillis = 500)
                        )
                    } else {
                        animatableX.animateTo(
                            targetValue = turnsInLevel[currentMove].first.toFloat(),
                            animationSpec = tween(durationMillis = 500)
                        )
                    }
                    mediaPlayer.release()
                }.join()


                gameState = gameState.copy(
                    playerPosition = Pair(
                        first = animatableX.value.toInt(),
                        second = animatableY.value.toInt()
                    )
                )

            } else {
                currentMove++
                if (gameState.playerPosition == gameState.path.last()) {
                    withContext(Dispatchers.Main) {
                        launch {
                            navController.navigate(Screens.GameChild.LevelSelect.route)
                            LevelController.getLevel().completed = true
                            FireBaseController.saveLevels()
                        }
                    }

                    resetAnimation()
                    return
                }

            }

        }
        delay(500)
        resetAnimation()
        resetPosition(navController.context)
    }

    private fun resetAnimation() {
        currentMove = 0
    }

    private suspend fun resetPosition(context: Context) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.speedsound)
        gameState = gameState.copy(playerPosition = startLocation)
        mediaPlayer.start()
        animatableX.animateTo(startLocation.first.toFloat())
        animatableY.animateTo(startLocation.second.toFloat())
        mediaPlayer.release()


    }

    fun back(navController: NavHostController) {
        navController.navigate(Screens.GameChild.LevelSelect.route) {
            popUpTo(Screens.GameChild.LevelSelect.route) {
                inclusive = true
            }
        }

    }

}

