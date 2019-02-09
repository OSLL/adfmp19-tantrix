package ru.hse.tantrix.model

import android.os.Parcel
import android.os.Parcelable
import ru.hse.tantrix.model.enums.NumberOfPlayers
import ru.hse.tantrix.model.enums.TimeMode
import ru.hse.tantrix.util.failWithParcel
import ru.hse.tantrix.util.readEnum

data class GameInfo(
        val players: List<PlayerInfo>,
        val numberOfPlayers: NumberOfPlayers,
        val timeMode: TimeMode
) : Parcelable {
    companion object CREATOR : Parcelable.Creator<GameInfo> {

        override fun createFromParcel(parcel: Parcel): GameInfo {
            val players = mutableListOf<PlayerInfo>()
            parcel.readTypedList(players, PlayerInfo.CREATOR)
            val numberOfPlayers = parcel.readEnum<NumberOfPlayers>() ?: failWithParcel("numberOfPlayers")
            val timeMode = parcel.readEnum<TimeMode>() ?: failWithParcel("timeMode")
            return GameInfo(players, numberOfPlayers, timeMode)
        }
        override fun newArray(size: Int): Array<GameInfo> {
            return Array(size) { GameInfo() }
        }

    }

    init {
        assert(players.size == numberOfPlayers.value)
    }

    private constructor() : this(emptyList(), NumberOfPlayers.DEFAULT, TimeMode.DEFAULT)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(players)
        parcel.writeSerializable(numberOfPlayers)
        parcel.writeSerializable(timeMode)
    }
    override fun describeContents(): Int {
        return 0
    }

    class GameInfoBuilder {
        var players: List<PlayerInfo> = emptyList()
        var numberOfPlayers = NumberOfPlayers.DEFAULT
        var timeMode: TimeMode = TimeMode.DEFAULT

        fun build(): GameInfo = GameInfo(players, numberOfPlayers, timeMode)
    }
}

