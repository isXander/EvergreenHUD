/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import kotlinx.serialization.Serializable

@Serializable
data class Color(val rgba: Int, val chroma: ChromaProperties = ChromaProperties.none) {
    constructor(r: Int, g: Int, b: Int, a: Int = 255, chroma: ChromaProperties = ChromaProperties.none) : this(
        a and 0xFF shl 24 or (r and 0xFF shl 16) or (g and 0xFF shl 8) or (b and 0xFF shl 0),
        chroma
    )

    constructor(r: Float, g: Float, b: Float, a: Float = 255f, chroma: ChromaProperties = ChromaProperties.none) : this(
        (a * 255).toInt(),
        (r * 255).toInt(),
        (g * 255).toInt(),
        (b * 255).toInt(),
        chroma
    )

    init {
        check(testColorRanges(this)) { "Color value out of range." }
    }

    val alpha: Int
        get() = rgba shr 24 and 0xFF
    val red: Int
        get() = rgba shr 16 and 0xFF
    val green: Int
        get() = rgba shr 8 and 0xFF
    val blue: Int
        get() = rgba shr 0 and 0xFF

    fun withRed(red: Int) = Color(red, green, blue, alpha, chroma)
    fun withGreen(green: Int) = Color(red, green, blue, alpha, chroma)
    fun withBlue(blue: Int) = Color(red, green, blue, alpha, chroma)
    fun withAlpha(alpha: Int) = Color(red, green, blue, alpha, chroma)
    fun withChroma(chroma: ChromaProperties) = Color(red, green, blue, alpha, chroma)

    companion object {
        fun testColorRanges(color: Color) =
            color.red in 0..255 && color.green in 0..255 && color.blue in 0..255 && color.alpha in 0..255

        val white = Color(255, 255, 255)
        val black = Color(0, 0, 0)
        val red = Color(255, 0, 0)
        val green = Color(0, 255, 0)
        val blue = Color(0, 0, 255)
        val yellow = Color(255, 255, 0)
        val cyan = Color(0, 255, 255)
        val magenta = Color(255, 0, 255)
    }

    @Serializable
    data class ChromaProperties(val hasChroma: Boolean, val chromaSpeed: Float = 2000f) {
        companion object {
            val none = ChromaProperties(false)
        }
    }
}
