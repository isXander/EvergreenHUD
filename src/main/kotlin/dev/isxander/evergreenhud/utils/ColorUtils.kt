/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.utils

fun extractRGBA(color: Int): RGBA {
    val a = (color shr 24 and 0xFF).toFloat() / 255.0f
    val r = (color shr 16 and 0xFF).toFloat() / 255.0f
    val g = (color shr 8 and 0xFF).toFloat() / 255.0f
    val b = (color and 0xFF).toFloat() / 255.0f

    return RGBA(r, g, b, a)
}

data class RGBA(val r: Float, val g: Float, val b: Float, val a: Float)
