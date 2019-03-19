package ru.hse.tantrix.model

import ru.hse.tantrix.drawable.HexagonDrawable
import ru.hse.tantrix.model.enums.TileType

class Tile(
    val type : TileType,
    val colors : TileColors
) : HexagonDrawable(colors.background) {

}