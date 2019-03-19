package ru.hse.tantrix.activities

import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import kotlinx.android.synthetic.main.activity_game.*
import ru.hse.tantrix.R
import ru.hse.tantrix.drawable.HexagonDrawable
import ru.hse.tantrix.model.GameInfo
import ru.hse.tantrix.model.PlayerInfo
import ru.hse.tantrix.model.enums.ColorMode
import ru.hse.tantrix.util.ExtraKeys
import ru.hse.tantrix.util.LogTags
import ru.hse.tantrix.util.normalize
import kotlin.properties.Delegates
import kotlin.random.Random


class GameActivity : AppCompatActivity() {
    private lateinit var fieldScaleGestureDetector: ScaleGestureDetector
    private lateinit var tilePickerGestureDetector: GestureDetectorCompat
    private var gameFieldScaleFactor = 1.0f
    private var enemyTiles : Int = -1

    private var colorMode: ColorMode by Delegates.observable(ColorMode.Mode1) { _, _, newValue ->
        colorPickerButton.text = colorMode.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val gameInfo = intent.getParcelableExtra<GameInfo>(ExtraKeys.GAME_INFO)

        configureButtons()
        configureGameFieldGestures()
        configureTilePicker()
        configureEnemiesLayout()
    }

    private fun configureTilePicker() {
        mainTile.setOnLongClickListener {
            val tag = it.tag as? CharSequence
            val item = ClipData.Item(tag)
            val dragData = ClipData(tag, arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item)
            val myShadow = View.DragShadowBuilder(it)
            it.startDragAndDrop(dragData, myShadow, null, 0)
        }
        val tmpTile = HexagonDrawable(R.color.gray)
        rightTile.background = tmpTile
        leftTile.background = tmpTile
        mainTile.background = HexagonDrawable(R.color.red)


        tilePickerGestureDetector = GestureDetectorCompat(this, TilePickerGestureListener(tilePickerLayout))
        tilePickerLayout.setOnTouchListener { _, event ->
            tilePickerGestureDetector.onTouchEvent(event)
            super.onTouchEvent(event)
        }
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

    }

    private fun configureEnemiesLayout() {
        fun setEnemyTilesVisible(enemy : Int) {
            enemiesTilesLayout.visibility = if (enemyTiles == -1) View.VISIBLE else View.INVISIBLE
            enemyTiles = if (enemyTiles == -1) enemyTiles else -1
        }

        // TODO : real tiles
        val tmpTile = HexagonDrawable(R.color.gray)
        enemyTile1.background = tmpTile
        enemyTile2.background = tmpTile
        enemyTile3.background = tmpTile
        enemyTile4.background = tmpTile
        enemyTile5.background = tmpTile
        enemyTile6.background = tmpTile

        enemyIcon1.setOnClickListener {setEnemyTilesVisible(1)}
        enemyIcon2.setOnClickListener {setEnemyTilesVisible(2)}
        enemyIcon3.setOnClickListener {setEnemyTilesVisible(3)}
    }

    private fun configureGameFieldGestures() {
        fieldScaleGestureDetector = ScaleGestureDetector(gameFieldLayout.context, FieldScaleGestureListener(gameFieldLayout))
        gameFieldLayout.setOnTouchListener { _, event -> fieldScaleGestureDetector.onTouchEvent(event) }
    }

    private inner class TilePickerGestureListener(private val view: View) : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(event: MotionEvent): Boolean {
            Log.d(LogTags.DEBUG, "On down")
            return true
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            Log.d(LogTags.DEBUG, "On fling")
            return true
        }

        override fun onScroll(event1: MotionEvent, event2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            Log.d(LogTags.DEBUG, "On scroll")
            return true
        }
    }

    private inner class FieldScaleGestureListener(private val view: View) : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {}

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if (!detector.isInProgress) return true

            gameFieldScaleFactor *= detector.scaleFactor
            gameFieldScaleFactor = gameFieldScaleFactor.normalize()
            view.scaleX = gameFieldScaleFactor
            view.scaleY = gameFieldScaleFactor
            return true
        }
    }
}

open class CircleIterator<T>(private val list: List<T>, protected var index: Int = 0) : Iterable<T> {
    fun next(): T {
        if (index++ >= list.size) index = 0
        return list[index]
    }

    fun prev(): T {
        if (index-- < 0) index = list.size - 1
        return list[index]
    }

    fun current(): T {
        return list[index]
    }

    override fun iterator(): Iterator<T> = list.iterator()
}

class MutableCircleIterator<T>(private val list: MutableList<T>, index: Int = 0) : CircleIterator<T>(list, index) {
    fun replaceCurrent(other: T) {
        list[index] = other
    }

    fun removeCurrent() {
        list.removeAt(index)
        if (index >= list.size) {
            index = 0
        }
    }
}

fun <T> List<T>.circleIterator(startIndex: Int = 0): CircleIterator<T> = CircleIterator(this, startIndex)
fun <T> MutableList<T>.mutableCircleIterator(startIndex: Int = 0): MutableCircleIterator<T> = MutableCircleIterator(this, startIndex)

fun <T> MutableList<T>.removeRandomly(): T = removeAt(Random.nextInt(size))

class Game(gameInfo: GameInfo) {
    private val players: CircleIterator<Player>
    private val tiles: MutableList<Tile> = Tile.ALL_TILES.toMutableList()

    init {
        players = gameInfo.players.map {
            val tiles = (1..6).mapTo(mutableListOf()) { tiles.removeRandomly() }
            Player(tiles, it)
        }.circleIterator()
    }

    val currentPlayer: Player
        get() = players.current()
}

// TODO
class Tile {
    companion object {
        const val NUMBER_OF_TILES = 54

        val ALL_TILES = (1..NUMBER_OF_TILES).map { Tile() }
    }
}

// TODO
class Player(tiles: MutableList<Tile>, val playerInfo: PlayerInfo) {
    init {
        assert(tiles.size <= 6)
    }

    val tiles = tiles.mutableCircleIterator()
}