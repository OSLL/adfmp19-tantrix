package ru.hse.tantrix.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_instructions_details.*
import ru.hse.tantrix.R
import ru.hse.tantrix.model.InstructionEntry
import ru.hse.tantrix.util.ExtraKeys

class InstructionsDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructions_details)
        val (_, title, description) = intent.getParcelableExtra(ExtraKeys.INSTRUCTION_ENTRY)
                ?: InstructionEntry.DEFAULT_ENTRY
        titleView.text = title
        descriptionView.text = description

        backButton.setOnClickListener {
            finish()
        }
    }
}
