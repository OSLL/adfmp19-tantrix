package ru.hse.tantrix.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
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

class ClassicGameInitialActivity : AppCompatActivity() {
    private var timeMode: TimeMode = TimeMode.DEFAULT
    private var numberOfPlayers: NumberOfPlayers = NumberOfPlayers.DEFAULT
    private var singlePlayerColor: TileColor = TileColor.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classic_game_initial)

        val gameMode: GameMode = intent.getSerializableExtra(ExtraKeys.GAME_MODE).safeAs<GameMode>().let {
            if (it == null || it == GameMode.Solitaire) {
                Log.d(ERROR, "Illegal game mode")
                TODO()
            }
            it
        }

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

        // Color:
        when (gameMode) {
            GameMode.WithBots -> {
                chooseColorButton.visibility = View.GONE
                chooseColorSpinner.visibility = View.VISIBLE
            }
            GameMode.Hotseat -> {
                chooseColorButton.visibility = View.VISIBLE
                chooseColorSpinner.visibility = View.GONE
            }
            GameMode.Solitaire-> emptyBranch()
        }

        // TODO: Color spinner
        chooseColorSpinner.adapter = EnumAdapter(this, android.R.layout.simple_spinner_item, TileColor.values()).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        chooseColorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                numberOfPlayers = NumberOfPlayers.valueOf(position)!!
            }
        }

        // TODO: Color button

        // Time mode
        timeLimitedSpinner.adapter = EnumAdapter(this, android.R.layout.simple_spinner_item, TimeMode.values()).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        timeLimitedSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                singlePlayerColor = TileColor.valueOf(position)!!
            }
        }

        // Start game button
        startGameButton.setOnClickListener {
            val gameInfo = GameInfo.GameInfoBuilder().apply {
                numberOfPlayers = this@ClassicGameInitialActivity.numberOfPlayers
                timeMode = this@ClassicGameInitialActivity.timeMode
                players = when (gameMode) {
                    GameMode.WithBots -> listOf(getSinglePlayerInfo()) + getBotsInfos()
                    GameMode.Hotseat -> TODO()
                    GameMode.Solitaire -> emptyBranch()
                }
            }.build()

            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra(ExtraKeys.GAME_INFO, gameInfo)
            }
            startActivity(intent)
        }
    }

    private fun getSinglePlayerInfo(): PlayerInfo = PlayerInfo(PlayerType.Player, singlePlayerColor, TurnSequence.First)

    private fun getBotsInfos(): List<PlayerInfo> {
        val freeColors = mutableSetOf(*TileColor.values())
        freeColors.remove(singlePlayerColor)
        val bots = mutableListOf<PlayerInfo>()
        for (number in 2..numberOfPlayers.value) {
            val color = freeColors.first()
            freeColors.remove(color)
            bots.add(PlayerInfo(PlayerType.Bot, color, TurnSequence.fromTurn(number)))
        }
        return bots
    }
}