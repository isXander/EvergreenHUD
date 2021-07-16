package dev.isxander.evergreenhud.compatibility.universal.impl

abstract class AIFontRenderer {

    abstract fun draw(text: String, x: Float, y: Float, color: Int, shadow: Boolean = true, centered: Boolean = false): AIFontRenderer

    abstract val fontHeight: Int
    abstract fun width(text: String): Int

}