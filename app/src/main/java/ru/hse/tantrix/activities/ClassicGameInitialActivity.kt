package ru.hse.tantrix.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_classic_game_initial.*
import ru.hse.tantrix.R
import ru.hse.tantrix.adapters.EnumAdapter
import ru.hse.tantrix.model.GameInfo
import ru.hse.tantrix.model.PlayerInfo
import ru.hse.tantrix.model.enums.*
import ru.hse.tantrix.util.ExtraKeys
import ru.hse.tantrix.util.LogTags.ERROR
import ru.hse.tantrix.util.emptyBranch
import ru.hse.tantrix.util.safeAs
import kotlin.properties.Delegates

class ClassicGameInitialActivity : AppCompatActivity() {
    private var multiplayer: Boolean = false
    private var timeMode: TimeMode = TimeMode.DEFAULT
    private var numberOfPlayers: NumberOfPlayers by Delegates.observable(NumberOfPlayers.DEFAULT) { _, _, newNumber ->
        if (multiplayer) {
            enableColorPickers(newNumber.value)
        }
    }

    private val playersColors: MutableList<LineColor> = LineColor.values().toMutableList()
    private lateinit var colorPickers: List<View>
    private lateinit var colorSpinners: List<Spinner>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classic_game_initial)

        colorPickers = listOf(
                player1ColorPicker,
                player2ColorPicker,
                player3ColorPicker,
                player4ColorPicker
        )

        colorSpinners = listOf(
                player1ColorSpinner,
                player2ColorSpinner,
                player3ColorSpinner,
                player4ColorSpinner
        )

        val gameMode: GameMode = intent.getSerializableExtra(ExtraKeys.GAME_MODE).safeAs<GameMode>().let {
            if (it == null || it == GameMode.Solitaire) {
                Log.d(ERROR, "Illegal game mode")
                TODO()
            }
            it
        }

        multiplayer = gameMode == GameMode.Hotseat
        numberOfPlayers = NumberOfPlayers.DEFAULT

        titleView.text = when (gameMode) {
            GameMode.WithBots -> getString(R.string.against_bots)
            GameMode.Hotseat -> getString(R.string.hotseat)
            GameMode.Solitaire -> emptyBranch()
        }

        // Number of Players
        numberOfPlayersSpinner.adapter = EnumAdapter(this, android.R.layout.simple_spinner_item, NumberOfPlayers.values()).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        numberOfPlayersSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                numberOfPlayers = NumberOfPlayers.valueOf(position)!!
            }
        }


        // Color spinners
        colorSpinners.forEachIndexed { index, spinner -> spinner.configureColorSpinner(index) }

        // Time mode
        timeLimitedSpinner.adapter = EnumAdapter(this, android.R.layout.simple_spinner_item, TimeMode.values()).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        timeLimitedSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                timeMode = TimeMode.valueOf(position)!!
            }
        }

        // Start game button
        startGameButton.setOnClickListener {
            if (multiplayer && !checkPlayersColors()) {
                Toast.makeText(this@ClassicGameInitialActivity, getString(R.string.error_message_unique_colors), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val gameInfo = GameInfo.GameInfoBuilder().apply {
                numberOfPlayers = this@ClassicGameInitialActivity.numberOfPlayers
                timeMode = this@ClassicGameInitialActivity.timeMode

                players = when (gameMode) {
                    GameMode.WithBots -> listOf(getSinglePlayerInfo()) + getBotsInfos()
                    GameMode.Hotseat -> getPlayersInfo()
                    GameMode.Solitaire -> emptyBranch()
                }
            }.build()

            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra(ExtraKeys.GAME_INFO, gameInfo)
            }
            startActivity(intent)
        }
    }

    private fun Spinner.configureColorSpinner(playerNumber: Int) {
        adapter = ru.hse.tantrix.adapters.EnumAdapter(this@ClassicGameInitialActivity, android.R.layout.simple_spinner_item, ru.hse.tantrix.model.enums.LineColor.values()).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                playersColors[playerNumber] = LineColor.valueOf(position)!!
            }
        }
    }

    // used for mode with bots
    private fun getSinglePlayerInfo(): PlayerInfo = PlayerInfo(PlayerType.Player, playersColors[0], TurnSequence.First)

    // used for mode with bots
    private fun getBotsInfos(): List<PlayerInfo> {
        val freeColors = mutableSetOf(*LineColor.values())
        freeColors.remove(playersColors[0])
        val bots = mutableListOf<PlayerInfo>()
        for (number in 2..numberOfPlayers.value) {
            val color = freeColors.first()
            freeColors.remove(color)
            bots.add(PlayerInfo(PlayerType.Bot, color, TurnSequence.fromTurn(number)))
        }
        return bots
    }

    // used for hotseat mode
    private fun getPlayersInfo(): List<PlayerInfo> {
        val infos = mutableListOf<PlayerInfo>()
        for (index in 0 until numberOfPlayers.value) {
            infos.add(PlayerInfo(PlayerType.Player, playersColors[index], TurnSequence.valueOf(index)!!))
        }
        return infos
    }

    private fun checkPlayersColors(): Boolean {
        val number = numberOfPlayers.value
        return playersColors.asSequence().take(number).distinct().count() == number
    }

    private fun disableAllColorPickers() {
        for (picker in colorPickers) {
            picker.visibility = View.GONE
        }
    }

    private fun enableColorPickers(number: Int) {
        disableAllColorPickers()
        for (index in 0 until number) {
            colorPickers[index].visibility = View.VISIBLE
        }
    }
}