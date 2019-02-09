package ru.hse.tantrix.model.enums

import ru.hse.tantrix.R
import ru.hse.tantrix.adapters.EnumWithName

// TODO: think about choosing time limit
enum class TimeMode(override val nameId: Int) : EnumWithName {
    LimitedTime(R.string.limited_time),
    UnlimitedTime(R.string.unlimited_time);

    companion object {
        val DEFAULT: TimeMode = LimitedTime

        fun valueOf(position: Int): TimeMode? {
            return values().getOrNull(position)
        }
    }
}