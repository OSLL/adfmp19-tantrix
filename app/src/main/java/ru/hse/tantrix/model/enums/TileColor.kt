package ru.hse.tantrix.model.enums

import ru.hse.tantrix.R
import ru.hse.tantrix.adapters.EnumWithName

enum class TileColor(override val nameId: Int, val colorId: Int) : EnumWithName {
    Red(R.string.red, R.color.red),
    Blue(R.string.blue, R.color.blue),
    Green(R.string.green, R.color.green),
    Yellow(R.string.yellow, R.color.yellow);

    companion object {
        val DEFAULT: TileColor = Red

        fun valueOf(position: Int): TileColor? {
            return TileColor.values().getOrNull(position)
        }
    }
}