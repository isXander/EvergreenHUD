/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import kotlin.math.max
import kotlin.math.sqrt

interface Rectangle {
    val x1: Float
    val y1: Float
    val x2: Float
    val y2: Float

    fun distFrom(x: Float, y: Float): Float {
        val dx = max(x1 - x, x - x2).coerceAtLeast(0f)
        val dy = max(y1 - y, y - y2).coerceAtLeast(0f)
        return sqrt(dx*dx + dy*dy)
    }

    fun overlaps(x: Float, y: Float): Boolean =
        distFrom(x, y) == 0f
}

data class SimpleRectangle(override val x1: Float, override val y1: Float, override val x2: Float, override val y2: Float) : Rectangle

class HitBox2D(
    override val x1: Float, override val y1: Float,
    val width: Float, val height: Float
) : Rectangle {
    override val x2 = x1 + width
    override val y2 = y1 + height
}
