package ru.hse.tantrix.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main_menu.*
import ru.hse.tantrix.R
import ru.hse.tantrix.model.GameMode
import ru.hse.tantrix.util.disable
import ru.hse.tantrix.util.enable
import kotlin.properties.Delegates.observable

class MainMenuActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MAIN_MENU"
    }

    private var gameMode: GameMode by observable(GameMode.WithBots) {
        _, _, newMod -> gameModButton.text = getString(newMod.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        gameModButton.setOnClickListener {
            Log.d(TAG, "mode pressed")
            gameModeLayout.visibility = View.VISIBLE
            mainMenuLayout.disable()
        }
        solitaireButton.setOnClickListener { chooseMode(GameMode.Solitaire) }
        withBotsButton.setOnClickListener { chooseMode(GameMode.WithBots) }
        hotseatButton.setOnClickListener { chooseMode(GameMode.Hotseat) }
        instructionsButton.setOnClickListener {
            startActivity(Intent(this, InstructionsActivity::class.java))
        }
    }

    private fun chooseMode(gameMode: GameMode) {
        Log.d(TAG, "mode choosed: $gameMode")
        this.gameMode = gameMode
        gameModeLayout.visibility = View.GONE
        mainMenuLayout.enable()
    }
}
