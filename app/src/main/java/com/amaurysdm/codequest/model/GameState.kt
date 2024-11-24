package com.amaurysdm.codequest.model

data class GameState(var playerPosition: Pair<Int, Int>, val path: List<Pair<Int, Int>>)