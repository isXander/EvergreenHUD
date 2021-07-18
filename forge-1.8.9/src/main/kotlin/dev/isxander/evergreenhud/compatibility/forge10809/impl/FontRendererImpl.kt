package dev.isxander.evergreenhud.compatibility.forge10809.impl

import dev.isxander.evergreenhud.compatibility.forge10809.mc
import dev.isxander.evergreenhud.compatibility.universal.impl.AIFontRenderer

class FontRendererImpl : AIFontRenderer() {
    override fun draw(text: String, x: Float, y: Float, color: Int, shadow: Boolean): AIFontRenderer {
        mc.fontRendererObj.drawString(text, x, y, color, shadow)

        return this
    }

    override val fontHeight: Int
        get() = mc.fontRendererObj.FONT_HEIGHT

    override fun width(text: String): Int = mc.fontRendererObj.getStringWidth(text)
}