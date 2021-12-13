/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.utils

data class HitBox2D(
    val x: Float, val y: Float,
    val width: Float, val height: Float
) {
    fun doesPositionOverlap(x: Float, y: Float): Boolean {
        return x >= this.x && x <= this.x + width && y >= y && y <= y + height
    }

    fun doesPositionOverlap(hitbox: HitBox2D): Boolean = doesPositionOverlap(hitbox.x, hitbox.y)
}
