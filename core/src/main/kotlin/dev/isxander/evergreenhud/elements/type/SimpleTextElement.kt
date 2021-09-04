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
import dev.isxander.evergreenhud.settings.impl.OptionContainer
import dev.isxander.evergreenhud.settings.impl.OptionSetting
import dev.isxander.evergreenhud.utils.drawString
import java.lang.StringBuilder
import kotlin.math.max

abstract class SimpleTextElement : TextElement() {

    @OptionSetting(name = "Title Location", category = "Text", description = "Where to display the title.")
    var titleLocation = TitleLocation.BEGINNING

    var cachedDisplayString: String = "Calculating..."
        private set

    // not sure whether to make this a kotlin property or not
    // I find get() {} very ugly and if I want to add any arguments
    // it is super easy
    protected abstract fun calculateValue(): String

    private val displayString: String
        get() {
            val showTitle = title.trim().isNotEmpty()
            val builder = StringBuilder()

            if (brackets)
                builder.append("[")

            if (showTitle && titleLocation == TitleLocation.BEGINNING)
                builder.append("${title}: ")

            builder.append(calculateValue())

            if (showTitle && titleLocation == TitleLocation.END)
                builder.append(" $title")

            if (brackets)
                builder.append("]")

            return builder.toString()
        }

    override val hitboxWidth: Float
        get() = max(fontRenderer.width(cachedDisplayString), 10).toFloat()
    override val hitboxHeight: Float
        get() = max(fontRenderer.fontHeight, 10).toFloat()

    override fun render(deltaTicks: Float, renderOrigin: RenderOrigin) {
        if (renderCount == 0) cachedDisplayString = displayString
        renderCount++
        if (renderCount > cacheTime)
            renderCount = 0

        super.render(deltaTicks, renderOrigin)

        gl.push()
        gl.scale(position.scale, position.scale)

        var x = position.rawX / position.scale
        val y = position.rawY / position.scale

        if (alignment == Alignment.RIGHT)
            x -= fontRenderer.width(cachedDisplayString)

        drawString(
            cachedDisplayString,
            x, y,
            textColor.rgb,
            centered = alignment == Alignment.CENTER,
            shadow = textStyle == TextStyle.SHADOW,
            bordered = textStyle == TextStyle.BORDER,
            chroma = chroma, chromaSpeed = chromaSpeed.get().toFloat()
        )

        gl.pop()
    }

    object TitleLocation : OptionContainer() {
        val BEGINNING = option("Beginning", "Show the title at the start of the text.")
        val END = option("End", "Show the title at the end of text.")
    }

}