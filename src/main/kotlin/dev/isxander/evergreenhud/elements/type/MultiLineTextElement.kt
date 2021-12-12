/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.elements.type

import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.utils.drawString
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.int
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Formatting
import kotlin.math.max

abstract class MultiLineTextElement : TextElement() {
    var verticalSpacing by int(
        default = 2,
        name = "Vertical Spacing",
        category = "Text",
        description = "How far apart each line of text is from eachother.",
        min = 0,
        max = 5
    )

    var cachedDisplayString: ArrayList<String> = arrayListOf("Calculating...")
        private set

    private val displayString: ArrayList<String>
        get() {
            val value = calculateValue()
            if (brackets) value.replaceAll { line: String -> "[$line]" }

            if (!title.equals("", true)) {
                value.add(0, Formatting.BOLD.toString() + title)
            }
            return value
        }

    protected abstract fun calculateValue(): ArrayList<String>

    override val hitboxWidth: Float
        get() {
            var width = 10
            for (line in cachedDisplayString) width = max(width, mc.textRenderer.getWidth(line))
            return width.toFloat()
        }
    override val hitboxHeight: Float
        get() = max((mc.textRenderer.fontHeight * cachedDisplayString.size) + (verticalSpacing * (cachedDisplayString.size - 1)), 10).toFloat()

    override fun render(matrices: MatrixStack, renderOrigin: RenderOrigin) {
        if (renderCount == 0) cachedDisplayString = displayString
        renderCount++
        if (renderCount > cacheTime)
            renderCount = 0

        super.render(matrices, renderOrigin)

        matrices.push()
        matrices.scale(position.scale, position.scale, 1f)

        val x = position.rawX / position.scale
        val y = position.rawY / position.scale

        for ((i, line) in cachedDisplayString.withIndex()) {
            val posX = x - (if (alignment == Alignment.RIGHT) mc.textRenderer.getWidth(line) else 0)
            val posY = (y / position.scale) + (mc.textRenderer.fontHeight * i) + (verticalSpacing * i)

            drawString(
                matrices,
                line,
                posX, posY,
                textColor.rgb,
                centered = alignment == Alignment.CENTER,
                shadow = textStyle == TextStyle.SHADOW,
                bordered = textStyle == TextStyle.BORDER,
                chroma = chroma, chromaSpeed = chromaSpeed.toFloat()
            )
        }

        matrices.pop()
    }
}
