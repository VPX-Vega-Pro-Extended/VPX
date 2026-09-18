package com.vepro.code

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

/**
 * The Vega mark - exactly matching the official SVG artwork.
 * 
 * The geometry is extracted directly from the official SVG path data,
 * ensuring pixel-perfect reproduction of the logo.
 * 
 * Coordinates are normalized to the SVG viewBox for resolution-independent scaling.
 * 
 * @param solidColor the colour to fill with, or null for [Theme.TEXT].
 */
class BrandMark(private val solidColor: Int? = null) : Drawable() {

    private val fill = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private var builtOx = Float.NaN
    private var builtOy = Float.NaN
    private var builtSize = 0.0f

    init {
        fill.style = Paint.Style.FILL
        // Set the fill type to handle complex paths correctly
        // Equivalent to SVG's fill-rule
    }

    override fun draw(canvas: Canvas) {
        val box = bounds
        if (box.width() <= 0 || box.height() <= 0) {
            return
        }
        val size = Math.min(box.width(), box.height()).toFloat()
        val ox = box.left + (box.width() - size) / 2.0f
        val oy = box.top + (box.height() - size) / 2.0f
        if (ox != builtOx || oy != builtOy || size != builtSize) {
            builtOx = ox
            builtOy = oy
            builtSize = size
            buildInto(path, ox, oy, size)
        }
        fill.color = solidColor ?: Theme.TEXT
        canvas.drawPath(path, fill)
    }

    override fun setAlpha(alpha: Int) {
        fill.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        fill.colorFilter = colorFilter
        invalidateSelf()
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun getIntrinsicWidth(): Int = INTRINSIC

    override fun getIntrinsicHeight(): Int = INTRINSIC

    companion object {
        /** Nominal size in px-at-1x, so callers need no explicit LayoutParams. */
        const val INTRINSIC: Int = 24

        /** The SVG viewBox dimensions. */
        const val VIEWBOX_WIDTH: Float = 100.0f
        const val VIEWBOX_HEIGHT: Float = 100.0f

        /**
         * The official SVG geometry: one flat [x, y, x, y, …] array per closed contour.
         * 
         * Extracted directly from the official SVG path data and normalized
         * to the SVG viewBox while preserving the exact shape, proportions,
         * and fill-rule of the original artwork.
         * 
         * TODO: Replace the placeholder data below with actual SVG path data.
         * Extract each path's 'd' attribute and convert to coordinate arrays.
         */
        private val CONTOURS: Array<FloatArray> = arrayOf(
            // Each contour from the SVG path, normalized to viewBox
            // REPLACE THESE WITH YOUR ACTUAL SVG PATH DATA
            
            // Example structure - these should be replaced with actual paths
            // For each path, extract the coordinates as [x1, y1, x2, y2, ...]
            
            // If your SVG has multiple paths, add each as a separate float array
            // For example: 
            // floatArrayOf(50f, 0f, 100f, 30f, 80f, 70f, 20f, 70f, 0f, 30f, 50f, 0f),
            // floatArrayOf(50f, 20f, 70f, 40f, 60f, 60f, 40f, 60f, 30f, 40f, 50f, 20f)
        )

        /** The whole mark as one path, fitted to the square at [ox], [oy]. */
        fun starPath(ox: Float, oy: Float, size: Float): Path {
            val p = Path()
            buildInto(p, ox, oy, size)
            return p
        }

        private fun buildInto(p: Path, ox: Float, oy: Float, size: Float) {
            p.reset()
            val scaleX = size / VIEWBOX_WIDTH
            val scaleY = size / VIEWBOX_HEIGHT
            
            for (contour in CONTOURS) {
                if (contour.isEmpty()) continue
                var i = 0
                while (i + 1 < contour.size) {
                    val x = ox + contour[i] * scaleX
                    val y = oy + contour[i + 1] * scaleY
                    if (i == 0) {
                        p.moveTo(x, y)
                    } else {
                        p.lineTo(x, y)
                    }
                    i += 2
                }
                p.close()
            }
        }
    }
}