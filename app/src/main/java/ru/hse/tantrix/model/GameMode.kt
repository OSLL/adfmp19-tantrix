package ru.hse.tantrix.model

import ru.hse.tantrix.R

enum class GameMode(val id: Int) {
    Solitaire(R.string.solitaire),
    WithBots(R.string.against_bots),
    Hotseat(R.string.hotseat);
}