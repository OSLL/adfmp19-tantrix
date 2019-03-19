package ru.hse.tantrix.model

import ru.hse.tantrix.R
import ru.hse.tantrix.model.enums.LineColor

data class TileColors(
    val line1 : LineColor = LineColor.Red,
    val line2 : LineColor = LineColor.Blue,
    val line3 : LineColor = LineColor.Green,
    val background : Int = R.color.black
) {
    // TODO : add modes changing?
}