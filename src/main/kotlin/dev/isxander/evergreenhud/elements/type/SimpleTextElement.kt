/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.elements.type

import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.settxi.impl.OptionContainer
import dev.isxander.evergreenhud.utils.drawString
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.option
import net.minecraft.client.util.math.MatrixStack
import kotlin.math.max

abstract class SimpleTextElement : TextElement() {
    var titleLocation by option(TitleLocation.BEGINNING) {
        name = "Title Location"
        category = "Text"
        description = "Where to display the title."
    }

    var cachedDisplayString: String = "Calculating..."
        private set

    protected abstract fun calculateValue(): String

    private val displayString: String
        get() {
            val showTitle = title.trim().isNotEmpty()
            var builder = ""

            if (brackets)
                builder += "["

            if (showTitle && titleLocation == TitleLocation.BEGINNING)
                builder += "${title}: "

            builder += calculateValue()

            if (showTitle && titleLocation == TitleLocation.END)
                builder += " $title"

            if (brackets)
                builder += "]"

            return builder
        }

    override val hitboxWidth: Float
        get() = max(mc.textRenderer.getWidth(cachedDisplayString), 10).toFloat()
    override val hitboxHeight: Float
        get() = max(mc.textRenderer.fontHeight, 10).toFloat()

    override fun render(matrices: MatrixStack, renderOrigin: RenderOrigin) {
        super.render(matrices, renderOrigin)

        matrices.push()
        matrices.scale(position.scale, position.scale, 1f)

        var x = position.rawX / position.scale
        val y = position.rawY / position.scale

        if (alignment == Alignment.RIGHT)
            x -= mc.textRenderer.getWidth(cachedDisplayString)

        drawString(
            matrices,
            cachedDisplayString,
            x, y,
            textColor.rgb,
            centered = alignment == Alignment.CENTER,
            shadow = textStyle == TextStyle.SHADOW,
            bordered = textStyle == TextStyle.BORDER,
            chroma = chroma, chromaSpeed = chromaSpeed.toFloat()
        )

        matrices.pop()
    }

    override fun onClientTick() {
        if (clientTicks == 0) cachedDisplayString = displayString

        super.onClientTick()
    }

    object TitleLocation : OptionContainer() {
        val BEGINNING = option("Beginning", "Show the title at the start of the text.")
        val END = option("End", "Show the title at the end of text.")
    }
}
