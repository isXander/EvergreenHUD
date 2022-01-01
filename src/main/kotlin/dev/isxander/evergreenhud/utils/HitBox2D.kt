/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.utils

class HitBox2D(
    override val x1: Float, override val y1: Float,
    val width: Float, val height: Float
) : Rectangle {
    override val x2 = x1 + width
    override val y2 = y1 + height
}
