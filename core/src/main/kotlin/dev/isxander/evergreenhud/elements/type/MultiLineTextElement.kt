/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.elements.type

import dev.isxander.evergreenhud.api.fontRenderer
import dev.isxander.evergreenhud.api.gl
import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.settxi.impl.IntSetting
import dev.isxander.evergreenhud.utils.drawString
import gg.essential.universal.ChatColor
import kotlin.math.max

abstract class MultiLineTextElement : TextElement() {

    @IntSetting(name = "Vertical Spacing", category = "Text", description = "How far apart each line of text is.", min = 0, max = 5)
    var verticalSpacing = 2

    var cachedDisplayString: ArrayList<String> = arrayListOf("Calculating...")
        private set

    private val displayString: ArrayList<String>
        get() {
            val value = calculateValue()
            if (brackets) value.replaceAll { line: String -> "[$line]" }

            if (!title.equals("", true)) {
                value.add(0, ChatColor.BOLD.toString() + title)
            }
            return value
        }

    protected abstract fun calculateValue(): ArrayList<String>

    override val hitboxWidth: Float
        get() {
            var width = 10
            for (line in cachedDisplayString) width = max(width, fontRenderer.width(line))
            return width.toFloat()
        }
    override val hitboxHeight: Float
        get() = max((fontRenderer.fontHeight * cachedDisplayString.size) + (verticalSpacing * (cachedDisplayString.size - 1)), 10).toFloat()

    override fun render(deltaTicks: Float, renderOrigin: RenderOrigin) {
        if (renderCount == 0) cachedDisplayString = displayString
        renderCount++
        if (renderCount > cacheTime)
            renderCount = 0

        super.render(deltaTicks, renderOrigin)

        gl.push()
        gl.scale(position.scale, position.scale)

        val x = position.rawX / position.scale
        val y = position.rawY / position.scale

        for ((i, line) in cachedDisplayString.withIndex()) {
            val posX = x - (if (alignment == Alignment.RIGHT) fontRenderer.width(line) else 0)
            val posY = (y / position.scale) + (fontRenderer.fontHeight * i) + (verticalSpacing * i)

            drawString(
                line,
                posX, posY,
                textColor.rgb,
                centered = alignment == Alignment.CENTER,
                shadow = textStyle == TextStyle.SHADOW,
                bordered = textStyle == TextStyle.BORDER,
                chroma = chroma, chromaSpeed = chromaSpeed.get().toFloat()
            )
        }

        gl.pop()
    }
}