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



    /*

        var gameState by mutableStateOf(GameState(Pair(0, 0), mutableListOf()))
        var uniqueDirectionInGameState = gameState.path.toSet()
        var draggingItem by mutableStateOf<Directions>(Directions.Nothing)
        var isAnimating by mutableStateOf(false)
        var screenPosition by mutableStateOf(Offset.Zero)

        */
    /*var tilePositionX by mutableIntStateOf(0)
        var tilePositionY by mutableIntStateOf(0)*//*




    var topBarItems: MutableList<Directions> =
        MutableList(numberOfTurns()) {
            Directions.Nothing
        }

    var bottomBarItems: MutableList<Directions> =
        uniqueDirectionInGameState.toMutableList()

    private var usableTopBarItems = {
        topBarItems.forEach{}
    }

    fun numberOfTurns():Int {

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


    init {
        gameState = GameState(Pair(0, 0), createDirectionPath())
        uniqueDirectionInGameState = gameState.path.toSet()

    }

    private fun createDirectionPath(): MutableList<Directions>{
        val path = mutableListOf<Directions>()
        LevelController.getLevel().route.forEach { character ->
            path.add(mapDirectionsList.find{ it.direction == character} ?: Directions.Nothing)
        }
        return path
    }

    val pathCoordinates: () -> MutableList<Pair<Int, Int>> = {
        val path = createDirectionPath()
        val pathCoordinates = mutableListOf<Pair<Int,Int>>()
        var currentPosition = Pair(0,0)
        path.forEach { direction ->
            currentPosition = Pair(
                currentPosition.first + direction.movement.first,
                currentPosition.second + direction.movement.second
            )
            pathCoordinates.add(currentPosition)
        }
        pathCoordinates
    }


    suspend fun playButton(navController: NavHostController){
        //var usableTopBarItems = topBarItems.toMutableList()
        val pathCoordinates = pathCoordinates()
        var currentCoordinate = 0

        while (isAnimating){
            delay(150)
            */
    /*pathCoordinates().forEach { direction ->

                    playerPositionX = direction.first
                    playerPositionY = direction.second
                }*//*

            gameState.playerPosition = pathCoordinates[currentCoordinate]
            currentCoordinate++
            if (currentCoordinate == pathCoordinates.size){
                //currentCoordinate = 0
                isAnimating = false
            }


            */
    /*gameState.playerPosition = Pair(gameState.playerPosition.first + usableTopBarItems.first().movement.first,
                    gameState.playerPosition.second + usableTopBarItems.first().movement.first)*//*

        }*/
    /**//*

    }

*/


    /*    private val level = LevelController.getLevel()
        private var levelText = level.route
        private var currentMovingDirectionIndex by mutableIntStateOf(0)



        var currentState = createLevelFromText() // Has the path and start location

        val uniqueDirection = currentState.path.toSet()//levelText.toSet()

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

            //var currentPosition = startingPosition
            //val path = mutableListOf(currentPosition)
            val path = mutableListOf<Directions>()
            levelText.forEach { value ->
                //movementOptionsList.map {it.direction}.filter { it == value } .first()
                *//*currentPosition = when (value) {
                'u' -> Pair(currentPosition.first, currentPosition.second - 1)
                'd' -> Pair(currentPosition.first, currentPosition.second + 1)
                'r' -> Pair(currentPosition.first + 1, currentPosition.second)
                'l' -> Pair(currentPosition.first - 1, currentPosition.second)
                else -> {
                    Pair(currentPosition.first, currentPosition.second)
                }
            }*//*
            path.add(mapDirectionsList.filter { it.direction == value } .first())
        }
        return GameState(startingPosition, path)
    }

    private fun moveDirection(
        chars: Directions,
        //repeatThis: Pair<Pair<Char, Char>, Int> = Pair(Pair('d', 'r'), 5)
    ): List<Pair<Int, Int>> {
        val moveDirection = mutableListOf<Pair<Int, Int>>()

        when (chars.direction) {
            'u' -> moveDirection.add(Pair(0, -1))
            'd' -> moveDirection.add(Pair(0, 1))
            'r' -> moveDirection.add(Pair(1, 0))
            'l' -> moveDirection.add(Pair(-1, 0))
            *//*'/' -> Pair((moveDirection(repeatThis.first.first).first * repeatThis.second)
                    + (moveDirection(repeatThis.first.second).first * repeatThis.second)
                , (moveDirection(repeatThis.first.first).second * repeatThis.second) +
                        (moveDirection(repeatThis.first.second).second * repeatThis.second))*//*
            else -> Pair(0, 0)
        } // That point is in the path keep going intill you find it.
        return moveDirection
    }


    suspend fun movePlayer(chars: ArrayList<Directions>, navController: NavHostController) {
        var charsIterator = chars.iterator()

        fun isValidMove(x: Int, y: Int): Boolean {
            return currentState.path.map { it.movement }
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

            if (positionX == currentState.path.last().movement.first
                && positionY == currentState.path.last().movement.second
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
                    chars[currentMovingDirectionIndex] == Directions.Nothing
                ) {

                    positionX = currentState.path.first().movement.first
                    positionY = currentState.path.first().movement.second
                    currentMovingDirectionIndex = 0
                    isAnimating = false

                }
            }
        }

        isAnimating = false
    }*/

    /*    fun getImage(it: Char): Int {
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
        }*/
}

