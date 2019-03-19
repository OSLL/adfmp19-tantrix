package ru.hse.tantrix.model

import android.os.Parcel
import android.os.Parcelable
import ru.hse.tantrix.model.enums.PlayerType
import ru.hse.tantrix.model.enums.LineColor
import ru.hse.tantrix.model.enums.TurnSequence
import ru.hse.tantrix.util.failWithParcel
import ru.hse.tantrix.util.readEnum

data class PlayerInfo(val playerType: PlayerType, val color: LineColor, val turn: TurnSequence) : Parcelable {
    companion object CREATOR : Parcelable.Creator<PlayerInfo> {
        override fun createFromParcel(parcel: Parcel): PlayerInfo {
            val playerType = parcel.readEnum<PlayerType>() ?: failWithParcel("playerType")
            val color = parcel.readEnum<LineColor>() ?: failWithParcel("color")
            val turn = parcel.readEnum<TurnSequence>() ?: failWithParcel("turn")
            return PlayerInfo(playerType, color, turn)
        }

        override fun newArray(size: Int): Array<PlayerInfo> {
            return Array(size) { PlayerInfo() }
        }
    }

    private constructor() : this(PlayerType.DEFAULT, LineColor.DEFAULT, TurnSequence.DEFAULT)

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeSerializable(playerType)
            writeSerializable(color)
            writeSerializable(turn)
        }
    }

    override fun describeContents(): Int = 0
}