package ru.hse.tantrix.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_game.*
import ru.hse.tantrix.R
import ru.hse.tantrix.model.GameInfo
import ru.hse.tantrix.model.enums.ColorMode
import ru.hse.tantrix.util.ExtraKeys
import kotlin.properties.Delegates

class GameActivity : AppCompatActivity() {
    private var colorMode: ColorMode by Delegates.observable(ColorMode.Mode1) { _, _, newValue ->
        colorPickerButton.text = colorMode.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val gameInfo = intent.getParcelableExtra<GameInfo>(ExtraKeys.GAME_INFO)

        configureButtons()
    }

    private fun configureButtons() {
        // Picker
        val colorModButtons = mapOf(
                colorMode1Button to ColorMode.Mode1,
                colorMode2Button to ColorMode.Mode2,
                colorMode3Button to ColorMode.Mode3
        )
        colorPickerButton.setOnClickListener {
            colorModButtons.keys.forEach {
                it.visibility = View.VISIBLE
                it.isEnabled = true
            }
            shadowView.visibility = View.VISIBLE
        }

        // Color mods
        fun chooseColorMod(mode: ColorMode) {
            colorMode = mode
            colorModButtons.keys.forEach { it.visibility = View.INVISIBLE }
            shadowView.visibility = View.GONE
        }
        colorModButtons.forEach { button, mod -> button.setOnClickListener { chooseColorMod(mod) } }

        // Pause
        pauseButton.setOnClickListener {
            startActivity(Intent(this, PauseActivity::class.java))
        }

        // TODO: Enemies
    }


}
