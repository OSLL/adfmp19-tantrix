package ru.hse.tantrix.drawable

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.drawable.Drawable
import kotlin.math.*

open class HexagonDrawable(color: Int) : Drawable() {
    private val hexagon = Path()
    private val temporal = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.color = color
        hexagon.fillType = Path.FillType.EVEN_ODD
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPath(hexagon, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return paint.alpha
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        computeHex(bounds)
        invalidateSelf()
    }

    fun computeHex(bounds: Rect) {
        val width = bounds.width()
        val height = bounds.height()
        val size = Math.min(width, height)
        val centerX = bounds.left + width / 2
        val centerY = bounds.top + height / 2

        hexagon.reset()
        hexagon.addPath(createHexagon(size, centerX, centerY))
        hexagon.addPath(createHexagon((size * .8f).toInt(), centerX, centerY))
    }

    private fun createHexagon(size: Int, centerX: Int, centerY: Int): Path {
        val section = (2.0 * PI / SIDES).toFloat()
        val radius = size / 2
        val hex = temporal
        hex.reset()
        hex.moveTo(
            centerX + radius * cos(0f),
            centerY + radius * sin(0f)
        )

        for (i in 1 until SIDES) {
            hex.lineTo(
                centerX + radius * cos(section * i),
                centerY + radius * sin(section * i)
            )
        }

        hex.close()
        return hex
    }

    companion object {
        val SIDES = 6
    }
}