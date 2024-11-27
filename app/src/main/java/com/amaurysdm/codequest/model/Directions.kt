package com.amaurysdm.codequest.model

import com.amaurysdm.codequest.R

sealed class Directions(
    open val direction: Char,
    open val icon: Int,
    open val movement: Pair<Int, Int>
) {
    data object Up : Directions('u', R.drawable.baseline_arrow_upward_24, Pair(0, -1))
    data object Down : Directions('d', R.drawable.baseline_arrow_downward_24, Pair(0, 1))
    data object Left : Directions('l', R.drawable.baseline_arrow_back_24, Pair(-1, 0))
    data object Right : Directions('r', R.drawable.baseline_arrow_forward_24, Pair(1, 0))

    data object Nothing : Directions('!', R.drawable.baseline_remove_24, Pair(0, 0))
    data object Repeat : Directions('/', R.drawable.baseline_autorenew_24, Pair(0, 0))

}

val movableDirections = mutableListOf<Directions>(
    Directions.Up,
    Directions.Down,
    Directions.Left,
    Directions.Right,
    Directions.Repeat
)
