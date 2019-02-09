package ru.hse.tantrix.activities

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.activity_instructions.*
import ru.hse.tantrix.R
import ru.hse.tantrix.model.InstructionEntry
import ru.hse.tantrix.model.SaxInstructionsParser
import ru.hse.tantrix.util.ERROR

class InstructionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructions)
        initInstructions()
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun initInstructions() {
        val layout = instructionButtonsLayout

        val instructions: List<InstructionEntry> = try {
            val parser = SaxInstructionsParser()
            resources.openRawResource(R.raw.instructions).use(parser::parse)
        } catch (e: Resources.NotFoundException) {
            // TODO
            Log.e(ERROR, "No resource files: ${e.message}")
            emptyList()
        }

        for (instruction in instructions) {
            val button = Button(this).apply {
                text = instruction.title
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                setOnClickListener {
                    val intent = Intent(this@InstructionsActivity, InstructionsDetailsActivity::class.java).apply {
                        putExtra(InstructionsDetailsActivity.INSTRUCTION_ENTRY_EXTRA_KEY, instruction)
                    }
                    startActivity(intent)
                }
            }
            layout.addView(button)
        }
    }
}