package ru.hse.tantrix.model.enums

enum class TurnSequence {
    First, Second, Third, Fourth;

    companion object {
        val DEFAULT: TurnSequence = First

        fun valueOf(position: Int): TurnSequence? {
            return TurnSequence.values().getOrNull(position)
        }

        fun fromTurn(turn: Int): TurnSequence = when (turn) {
            1 -> First
            2 -> Second
            3 -> Third
            4 -> Fourth
            else -> throw IllegalArgumentException()
        }
    }
}