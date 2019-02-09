package ru.hse.tantrix.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main_menu.*
import ru.hse.tantrix.R
import ru.hse.tantrix.model.enums.GameMode
import ru.hse.tantrix.util.ExtraKeys
import ru.hse.tantrix.util.disable
import ru.hse.tantrix.util.enable
import kotlin.properties.Delegates.observable

class MainMenuActivity : AppCompatActivity() {
    private var gameMode: GameMode by observable(GameMode.WithBots) {
        _, _, newMod -> gameModButton.text = getString(newMod.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        gameModButton.setOnClickListener {
            gameModeLayout.visibility = View.VISIBLE
            mainMenuLayout.disable()
        }
        solitaireButton.setOnClickListener { chooseMode(GameMode.Solitaire) }
        withBotsButton.setOnClickListener { chooseMode(GameMode.WithBots) }
        hotseatButton.setOnClickListener { chooseMode(GameMode.Hotseat) }
        instructionsButton.setOnClickListener {
            startActivity(Intent(this, InstructionsActivity::class.java))
        }

        startGameButton.setOnClickListener {
            when (gameMode) {
                GameMode.Solitaire -> startActivity(Intent(this, SolitaireGameInitialActivity::class.java))
                GameMode.WithBots, GameMode.Hotseat -> {
                    val intent = Intent(this, ClassicGameInitialActivity::class.java).apply {
                        putExtra(ExtraKeys.GAME_MODE, gameMode)
                    }
                    startActivity(intent)
                }
            }
        }
    }

    private fun chooseMode(gameMode: GameMode) {
        this.gameMode = gameMode
        gameModeLayout.visibility = View.GONE
        mainMenuLayout.enable()
    }
}
