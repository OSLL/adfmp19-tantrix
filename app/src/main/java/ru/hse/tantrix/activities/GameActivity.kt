package ru.hse.tantrix.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.content_game.*
import ru.hse.tantrix.R
import ru.hse.tantrix.model.GameInfo
import ru.hse.tantrix.util.ExtraKeys
import ru.hse.tantrix.util.LogTags.DEBUG

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        setSupportActionBar(toolbar)

        val gameInfo = intent.getParcelableExtra<GameInfo>(ExtraKeys.GAME_INFO)

        // TODO: remove this
        val info = gameInfo.toString()
        debugView.text = info
        Log.d(DEBUG, info)
    }

}
