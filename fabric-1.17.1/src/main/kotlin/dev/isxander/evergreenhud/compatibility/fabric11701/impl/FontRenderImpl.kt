package dev.isxander.evergreenhud.compatibility.fabric11701.impl

import dev.isxander.evergreenhud.compatibility.fabric11701.Main
import dev.isxander.evergreenhud.compatibility.fabric11701.mc
import dev.isxander.evergreenhud.compatibility.universal.impl.AIFontRenderer

class FontRenderImpl : AIFontRenderer() {
    override fun draw(text: String, x: Float, y: Float, color: Int, shadow: Boolean): AIFontRenderer {
        if (shadow) mc.textRenderer.drawWithShadow(Main.matrices, text, x, y, color)
        else mc.textRenderer.draw(Main.matrices, text, x, y, color)

        return this
    }

    override val fontHeight: Int
        get() = mc.textRenderer.fontHeight

    override fun width(text: String): Int = mc.textRenderer.getWidth(text)
}