package com.amaurysdm.codequest.model

import com.amaurysdm.codequest.R

sealed class Directions(val direction: Char, val icon: Int) {
    data object Up : Directions('u', R.drawable.baseline_arrow_upward_24)
    data object Down : Directions('d', R.drawable.baseline_arrow_downward_24)
    data object Left : Directions('l', R.drawable.baseline_arrow_back_24)
    data object Right : Directions('r', R.drawable.baseline_arrow_forward_24)
}