package ru.hse.tantrix.model.enums

import ru.hse.tantrix.R
import ru.hse.tantrix.adapters.EnumWithName

enum class LineColor(override val nameId: Int, val colorId: Int) : EnumWithName {
    Red(R.string.red, R.color.red),
    Blue(R.string.blue, R.color.blue),
    Green(R.string.green, R.color.green),
    Yellow(R.string.yellow, R.color.yellow);

    companion object {
        val DEFAULT: LineColor = Red

        fun valueOf(position: Int): LineColor? {
            return LineColor.values().getOrNull(position)
        }
    }
}