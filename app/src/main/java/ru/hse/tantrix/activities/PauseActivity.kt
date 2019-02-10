package ru.hse.tantrix.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pause.*
import ru.hse.tantrix.R

class PauseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pause)

        // TODO: fix
        exitGameButton.setOnClickListener {
            startActivity(Intent(this, MainMenuActivity::class.java))
        }

        instructionsButton.setOnClickListener {
            startActivity(Intent(this, InstructionsActivity::class.java))
        }

        resumeGameButton.setOnClickListener { finish() }

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
