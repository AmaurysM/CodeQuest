package com.amaurysdm.codequest.model

data class GameState(var playerPosition: Pair<Int, Int>, val path: MutableList<Pair<Int, Int>>)