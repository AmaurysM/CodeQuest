package com.amaurysdm.codequest.model

sealed class MovementOptions(
    val direction: Char,
    val icon: Int,
    val description: String,
    open var movement: MutableList<Pair<Int, Int>> = mutableListOf(Pair(0, 0))
) {
    data object Up : MovementOptions('u', 0, "Up", mutableListOf(Pair(0, -1)))
    data object Down : MovementOptions('d', 0, "Down", mutableListOf(Pair(0,1)))
    data object Right : MovementOptions('r', 0, "Right", mutableListOf(Pair(1,0)))
    data object Left : MovementOptions('l', 0, "Left", mutableListOf(Pair (-1,0)))

    data class Sequence(val directions: MutableList<MovementOptions> = movementOptionsList)
        : MovementOptions('/', 0, "Repeat", directions.map { it.movement }.flatten().toMutableList())

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