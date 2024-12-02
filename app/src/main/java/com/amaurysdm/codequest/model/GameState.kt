package com.amaurysdm.codequest.model

// Manages game state, the position of the player and the path he needs to pass
// The path is a list of points on a graph.
data class GameState(var playerPosition: Pair<Int, Int>, val path: MutableList<Pair<Int, Int>>)