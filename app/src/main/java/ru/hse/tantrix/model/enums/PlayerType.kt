package ru.hse.tantrix.model.enums

enum class PlayerType {
    Player, Bot;

    companion object {
        val DEFAULT: PlayerType = Player
    }
}