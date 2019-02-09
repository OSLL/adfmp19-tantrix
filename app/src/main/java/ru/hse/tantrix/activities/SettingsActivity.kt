package ru.hse.tantrix.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_settings.*
import ru.hse.tantrix.R
import ru.hse.tantrix.model.enums.Language
import ru.hse.tantrix.util.disable
import ru.hse.tantrix.util.enable

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        languageButton.setOnClickListener {
            languageLayout.visibility = View.VISIBLE
            settingsMenuLayout.disable()
        }

        // TODO: colors button

        // TODO: hints switch

        // TODO: back button
        backButton.setOnClickListener { finish() }

        configureLanguagePicker()
    }

    private fun configureLanguagePicker() {
        englishButton.setOnClickListener { chooseLanguage(Language.English) }
        russianButton.setOnClickListener { chooseLanguage(Language.Russian) }
    }

    private fun chooseLanguage(language: Language) {
        // TODO: set language
        languageLayout.visibility = View.GONE
        settingsMenuLayout.enable()
    }
}
