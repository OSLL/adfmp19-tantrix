package ru.hse.tantrix.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_instructions_details.*
import ru.hse.tantrix.R
import ru.hse.tantrix.model.InstructionEntry

class InstructionsDetailsActivity : AppCompatActivity() {
    companion object {
        const val INSTRUCTION_ENTRY_EXTRA_KEY = "InstructionEntry"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructions_details)
        val (_, title, description) = intent.getParcelableExtra(INSTRUCTION_ENTRY_EXTRA_KEY)
                ?: InstructionEntry.DEFAULT_ENTRY
        titleView.text = title
        descriptionView.text = description

        backButton.setOnClickListener {
            finish()
        }
    }
}
