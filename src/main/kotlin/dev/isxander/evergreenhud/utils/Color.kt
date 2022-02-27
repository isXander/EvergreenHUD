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
    constructor(rgba: Int, hasAlpha: Boolean, chroma: ChromaProperties = ChromaProperties.none) : this(if (hasAlpha) rgba else -0x1000000 or rgba, chroma)

    constructor(r: Int, g: Int, b: Int, a: Int = 255, chroma: ChromaProperties = ChromaProperties.none) : this(
        a and 0xFF shl 24 or (r and 0xFF shl 16) or (g and 0xFF shl 8) or (b and 0xFF shl 0),
        chroma
    )

    constructor(r: Float, g: Float, b: Float, a: Float = 1f, chroma: ChromaProperties = ChromaProperties.none) : this(
        (r * 255).toInt(),
        (g * 255).toInt(),
        (b * 255).toInt(),
        (a * 255).toInt(),
        chroma
    )

    constructor(color: java.awt.Color) : this(color.red, color.green, color.blue, color.alpha)

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

    val awt: java.awt.Color
        get() = java.awt.Color(rgba, true)

    fun withRed(red: Int) = Color(red, green, blue, alpha, chroma)
    fun withGreen(green: Int) = Color(red, green, blue, alpha, chroma)
    fun withBlue(blue: Int) = Color(red, green, blue, alpha, chroma)
    fun withAlpha(alpha: Int) = Color(red, green, blue, alpha, chroma)

    fun withRed(red: Float) = Color(red, green / 255f, blue / 255f, alpha / 255f, chroma)
    fun withGreen(green: Float) = Color(red / 255f, green, blue / 255f, alpha / 255f, chroma)
    fun withBlue(blue: Float) = Color(red / 255f, green / 255f, blue, alpha / 255f, chroma)
    fun withAlpha(alpha: Float) = Color(red / 255f, green / 255f, blue / 255f, alpha, chroma)

    fun withChroma(chroma: ChromaProperties) = Color(red, green, blue, alpha, chroma)

    companion object {
        fun testColorRanges(color: Color) =
            color.red in 0..255
                    && color.green in 0..255
                    && color.blue in 0..255
                    && color.alpha in 0..255

        val none = Color(0, 0, 0, 0)
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
    data class ChromaProperties(val hasChroma: Boolean, val chromaSpeed: Float = 2000f, val chromaFrequency: Float = 4f) {
        fun getChroma(x: Float, y: Float): Int {
            val c = ((chromaFrequency * (-2.0 * x.toDouble() + y.toDouble())) / 10.0) % 1.0
            return java.awt.Color.HSBtoRGB(((c + ((System.currentTimeMillis() % chromaSpeed.toDouble()).toInt() / chromaSpeed.toDouble())) % 1.0).toFloat(), 0.8f, 0.8f)
        }

        companion object {
            val none = ChromaProperties(false)
            val default = ChromaProperties(true)
        }
    }
}

fun extractRGBA(color: Int): RGBA {
    val a = (color shr 24 and 0xFF).toFloat() / 255.0f
    val r = (color shr 16 and 0xFF).toFloat() / 255.0f
    val g = (color shr 8 and 0xFF).toFloat() / 255.0f
    val b = (color and 0xFF).toFloat() / 255.0f

    return RGBA(r, g, b, a)
}

data class RGBA(val r: Float, val g: Float, val b: Float, val a: Float)
