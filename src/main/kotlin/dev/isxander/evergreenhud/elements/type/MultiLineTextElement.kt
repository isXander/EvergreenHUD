/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.type

import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.event.ClientTickEvent
import dev.isxander.evergreenhud.utils.drawString
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.int
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Formatting
import kotlin.math.max

abstract class MultiLineTextElement
@JvmOverloads constructor(title: String, cacheTime: Int = 5) : TextElement(title, cacheTime) {
    var verticalSpacing by int(2) {
        name = "Vertical Spacing"
        category = "Text"
        description = "How far apart each line of text is from eachother."
        range = 0..5
    }

    val cachedDisplayString by eventReturnable<ClientTickEvent, MutableList<String>>(mutableListOf("Calculating..."), { clientTicks == 0 }) {
        displayString
    }

    private val displayString: MutableList<String>
        get() {
            val value = calculateValue()
            if (brackets) value.replaceAll { line: String -> "[$line]" }

            if (!title.equals("", true)) {
                value.add(0, Formatting.BOLD.toString() + title)
            }
            return value
        }

    protected abstract fun calculateValue(): MutableList<String>

    override val hitboxWidth: Float
        get() {
            var width = 10
            for (line in cachedDisplayString!!) width = max(width, mc.textRenderer.getWidth(line))
            return width.toFloat()
        }
    override val hitboxHeight: Float
        get() = max((mc.textRenderer.fontHeight * cachedDisplayString!!.size) + (verticalSpacing * (cachedDisplayString!!.size - 1)), 10).toFloat()

    override fun render(matrices: MatrixStack, renderOrigin: RenderOrigin) {
        super.render(matrices, renderOrigin)

        matrices.push()
        matrices.scale(position.scale, position.scale, 1f)

        val x = position.rawX / position.scale
        val y = position.rawY / position.scale

        for ((i, line) in cachedDisplayString!!.withIndex()) {
            val posX = x - (if (alignment == Alignment.RIGHT) mc.textRenderer.getWidth(line) else 0)
            val posY = y + (mc.textRenderer.fontHeight * i) + (verticalSpacing * i)

            drawString(
                matrices,
                line,
                posX, posY,
                textColor.rgba,
                centered = alignment == Alignment.CENTER,
                shadow = textStyle == TextStyle.SHADOW,
                bordered = textStyle == TextStyle.BORDER,
                chroma = textColor.chroma
            )
        }

        matrices.pop()
    }
}
