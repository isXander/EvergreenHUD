/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

class HitBox2D(
    override val x1: Float, override val y1: Float,
    val width: Float, val height: Float
) : Rectangle {
    override val x2 = x1 + width
    override val y2 = y1 + height
}
