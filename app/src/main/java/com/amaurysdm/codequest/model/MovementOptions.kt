package com.amaurysdm.codequest.model

import com.amaurysdm.codequest.R

sealed class MovementOptions(
    val direction: Char,
    val icon: Int,
    val description: String,
    open var movement: MutableList<Pair<Int, Int>> = mutableListOf(Pair(0, 0)),
) {
    data object Up : MovementOptions('u', R.drawable.baseline_arrow_upward_24, "Up", mutableListOf(Pair(0, -1)))
    data object Down : MovementOptions('d', R.drawable.baseline_arrow_downward_24, "Down", mutableListOf(Pair(0,1)))
    data object Right : MovementOptions('r', R.drawable.baseline_arrow_forward_24, "Right", mutableListOf(Pair(1,0)))
    data object Left : MovementOptions('l', R.drawable.baseline_arrow_back_24, "Left", mutableListOf(Pair (-1,0)))

    data class Sequence(val directions: MutableList<MovementOptions> = movementOptionsList)
        : MovementOptions('/', R.drawable.baseline_home_24, "Repeat", directions.map { it.movement }.flatten().toMutableList())

    data object Nothing : MovementOptions(' ', 0, "Nothing", mutableListOf(Pair(0,0)))
}

val movementOptionsList = mutableListOf<MovementOptions>(
    MovementOptions.Up,
    MovementOptions.Down,
    MovementOptions.Right,
    MovementOptions.Left,
    MovementOptions.Sequence(),
    MovementOptions.Nothing
)