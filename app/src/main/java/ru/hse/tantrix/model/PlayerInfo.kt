package ru.hse.tantrix.model

import android.os.Parcel
import android.os.Parcelable
import ru.hse.tantrix.model.enums.PlayerType
import ru.hse.tantrix.model.enums.TileColor
import ru.hse.tantrix.model.enums.TurnSequence
import ru.hse.tantrix.util.failWithParcel
import ru.hse.tantrix.util.readEnum

data class PlayerInfo(val playerType: PlayerType, val color: TileColor, val turn: TurnSequence) : Parcelable {
    companion object CREATOR : Parcelable.Creator<PlayerInfo> {
        override fun createFromParcel(parcel: Parcel): PlayerInfo {
            val playerType = parcel.readEnum<PlayerType>() ?: failWithParcel("playerType")
            val color = parcel.readEnum<TileColor>() ?: failWithParcel("color")
            val turn = parcel.readEnum<TurnSequence>() ?: failWithParcel("turn")
            return PlayerInfo(playerType, color, turn)
        }

        override fun newArray(size: Int): Array<PlayerInfo> {
            return Array(size) { PlayerInfo() }
        }
    }

    private constructor() : this(PlayerType.DEFAULT, TileColor.DEFAULT, TurnSequence.DEFAULT)

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeSerializable(playerType)
            writeSerializable(color)
            writeSerializable(turn)
        }
    }

    override fun describeContents(): Int = 0

    class PlayerInfoBuilder {
        val playerType: PlayerType = PlayerType.DEFAULT
        var color: TileColor = TileColor.DEFAULT
        var turn: TurnSequence = TurnSequence.DEFAULT

        fun build(): PlayerInfo = PlayerInfo(playerType, color, turn)
    }
}