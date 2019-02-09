package ru.hse.tantrix.model.enums

import ru.hse.tantrix.R
import ru.hse.tantrix.adapters.EnumWithName

enum class NumberOfPlayers(val value: Int, override val nameId: Int) : EnumWithName {
    Two(2, R.string.two_players),
    Three(3, R.string.three_players),
    Four(4, R.string.four_players);

    companion object {
        val DEFAULT: NumberOfPlayers = Two

        fun valueOf(position: Int): NumberOfPlayers? {
            return NumberOfPlayers.values().getOrNull(position)
        }
    }
}