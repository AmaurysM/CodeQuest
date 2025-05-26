package com.amaurysdm.codequest.ui.level

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amaurysdm.codequest.R
import com.amaurysdm.codequest.controllers.LevelController
import com.amaurysdm.codequest.model.Directions
import com.amaurysdm.codequest.model.GameState
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
    // Created all of this before I know about flows


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


    init {
        gameState = GameState(startLocation, createPathCoordinates())
        uniqueDirectionInGameState =
            movableDirections.filter { createDirectionPath().toSet().contains(it) }.toSet()
        topBarItems =
            mutableStateListOf<TopBarItem>().apply { repeat(numberOfTurns()) { add(TopBarItem()) } }
        bottomBarItems = uniqueDirectionInGameState.toMutableList()
    }


    // Creates a list of directions based on the current Level route
    private fun createDirectionPath(): MutableList<Directions> {
        val path = mutableListOf<Directions>()

        // Gets the route. Its just a string of letters
        // We then go thought that string and check if its in the movableDirections
        // If it is, we add it to the path
        LevelController.getLevel().route.forEach { character ->
            path.add(movableDirections.find { it.direction == character } ?: Directions.Nothing)
        }
        // This lets me create levels very easily
        return path
    }

    // Creates a list of coordinates based on the current Level
    private fun createPathCoordinates(): MutableList<Pair<Int, Int>> {
        // gets the path a list of directions, up, down, left, right
        val movableDirections = createDirectionPath()

        var currentPosition = gameState.playerPosition
        val pathCoordinates = mutableListOf<Pair<Int, Int>>(currentPosition)

        // Goes through the path and adds the coordinates to the list
        movableDirections.forEach { direction ->
            // We add the direction to the current position
            // and add it to the list
            currentPosition = Pair(
                currentPosition.first + direction.movement.first,
                currentPosition.second + direction.movement.second
            )
            // this creates the actual path out of the coordinates
            pathCoordinates.add(currentPosition)
        }
        return pathCoordinates
    }

    // Gets me the number of turns
    private fun numberOfTurns(): Int {
        // theres always a one move you can use
        // Forward.
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
        // I use this to calculate the number of boxes on the top bar.
    }

    // Made this later to help me find the coordinates of the turns.
    // Along side the location of the ending position.
    private fun locationOfTurnsAndFlag(points: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        // If there are less than 3 points there are no turns
        if (points.size < 3) return emptyList()

        val turns = mutableListOf<Pair<Int, Int>>()

        for (i in 1 until points.size - 1) {
            // Gets the points before and after the current point
            val previous = points[i - 1]
            val current = points[i]
            val next = points[i + 1]

            // Gets the vector between the points
            // i.e. the direction
            val vector1 = Pair(current.first - previous.first, current.second - previous.second)
            val vector2 = Pair(next.first - current.first, next.second - current.second)

            // Checks if the vector is perpendicular to the vector between the points
            // Meaning if they make a corner
            if (vector1.first * vector2.second != vector1.second * vector2.first) {
                turns.add(current)
            }

        }
        turns.add(points.last())
        return turns
        // I use this to animate the rock from point to point.
    }

    // Theres allot of useless data in the top bar
    // This parses the top bar and gets the usable items.
    fun usableTopBarItems(): MutableList<TopBarItem> {

        // First get rid of the nulls
        // There is something in the top bar, you just can't see it.
        // There are 'nothing' directions that need to be filtered out
        val usableTopBarItems =
            topBarItems.filter { it.direction.value != Directions.Nothing }.toMutableList()

        var i = 0
        while (i < usableTopBarItems.size) {
            val currentItem = usableTopBarItems[i] // get the first item in the list

            // if its a repeater we need to somehow get the data out of it.
            // Since there can be many repeaters in play this is very important
            if (currentItem.direction.value == Directions.Repeat) {

                // Again filter out the nulls
                val usableChildren =
                    currentItem.children.filter { it.direction.value != Directions.Nothing }

                // gets the number of times we need to loop
                val repeats = currentItem.repeater?.intValue

                // Then we need to remove the repeater If we don't it would stay in the list.
                // Making the list have a useless item.
                // The repeater has a movement value of (0,0)
                usableTopBarItems.removeAt(i)

                // Check if you left the repeater empty
                if (usableChildren.isEmpty()) {
                    break
                }

                // add the same two items several times
                repeat(repeats ?: 0) {
                    for (j in usableChildren.size - 1 downTo 0) {
                        usableTopBarItems.add(i, usableChildren[j])
                    }
                }

            } else {
                // increment to check for the next item.
                i++
            }
        }
        return usableTopBarItems
    }

    // Checks if the direction your moving in is valid
    private fun validMove(move: Directions): Boolean {
        // checks if the move your about to make is in the path.
        // If not return false.
        return gameState.path.map { it }
            .contains(
                Pair(
                    move.movement.first + gameState.playerPosition.first,
                    move.movement.second + gameState.playerPosition.second
                )
            )
    }


    suspend fun playButton(navController: NavHostController,context: Context) {
        val chosenDirection = usableTopBarItems()
        val turnsInLevel = locationOfTurnsAndFlag(gameState.path)
        val scope = CoroutineScope(coroutineContext)

        while (isAnimating) {
            // Wait a couple seconds before moving
            delay(150)
            if (chosenDirection.isEmpty()) break

            if (currentMove == chosenDirection.size) {
                break
            }

            // move if the player chose a valid direction
            if (validMove(chosenDirection[currentMove].direction.value)) {

                scope.launch {
                    val mediaPlayer = MediaPlayer.create(
                        context,
                        R.raw.speedsoundcut
                    )

                    try {
                        mediaPlayer?.start()

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
                    } finally {
                        mediaPlayer?.release()
                    }
                }.join()


                // Theres a better way to do this but time is my enemy
                gameState = gameState.copy(
                    playerPosition = Pair(
                        first = animatableX.value.toInt(),
                        second = animatableY.value.toInt()
                    )
                )

            }
            // If the player is at the end of the path they win.
            if (gameState.playerPosition == gameState.path.last()) {
                withContext(Dispatchers.Main) {
                    launch {
                        navController.navigate(Screens.GameChild.LevelSelect.route)
                        LevelController.updateAndSaveCurrentLevel(true)
                    }
                }

                currentMove = 0
                return
            }
            // Increment the current move
            currentMove++

        }
        delay(500)
        resetPosition(navController.context)
    }

    // resets the position of the player with an animation
    private suspend fun resetPosition(context: Context) {
        currentMove = 0
        val mediaPlayer = MediaPlayer.create(context, R.raw.speedsound)
        gameState = gameState.copy(playerPosition = startLocation)
        mediaPlayer.start()
        animatableX.snapTo(startLocation.first.toFloat())
        animatableY.snapTo(startLocation.second.toFloat())
        mediaPlayer.release()

    }

    // back button
    fun back(navController: NavHostController) {
        navController.navigate(Screens.GameChild.LevelSelect.route) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }

    }

}

