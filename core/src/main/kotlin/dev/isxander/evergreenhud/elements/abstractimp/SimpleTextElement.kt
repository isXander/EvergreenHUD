package dev.isxander.evergreenhud.elements.abstractimp

import dev.isxander.evergreenhud.compatibility.universal.FONT_RENDERER
import dev.isxander.evergreenhud.compatibility.universal.GL
import dev.isxander.evergreenhud.elements.RenderOrigin
import dev.isxander.evergreenhud.settings.impl.OptionContainer
import dev.isxander.evergreenhud.settings.impl.OptionSetting
import dev.isxander.evergreenhud.utils.GuiUtils
import gg.essential.elementa.font.DefaultFonts
import gg.essential.universal.UMatrixStack
import java.lang.StringBuilder
import kotlin.math.max

abstract class SimpleTextElement : TextElement() {

    @OptionSetting(name = "Title Location", category = ["Text"], description = "Where to display the title.")
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
        get() = max(DefaultFonts.ELEMENTA_MINECRAFT_FONT_RENDERER.getStringWidth(cachedDisplayString, 10f), 10f)
    override val hitboxHeight: Float
        get() = DefaultFonts.ELEMENTA_MINECRAFT_FONT_RENDERER.getStringHeight(cachedDisplayString, 10f)

    override fun render(matrices: UMatrixStack, deltaTicks: Float, renderOrigin: RenderOrigin) {
        if (renderCount == 0) cachedDisplayString = displayString
        renderCount++
        if (renderCount > cacheTime)
            renderCount = 0

        super.render(matrices, deltaTicks, renderOrigin)

        matrices.push()
        matrices.scale(position.scale, position.scale, 1f)

        var x = position.rawX / position.scale
        val y = position.rawY / position.scale

        if (alignment == Alignment.RIGHT)
            x = (position.rawX - FONT_RENDERER.width(cachedDisplayString)) / position.scale

        GuiUtils.drawString(
            matrices, DefaultFonts.ELEMENTA_MINECRAFT_FONT_RENDERER,
            cachedDisplayString, x, y,
            textColor,
            centered = alignment == Alignment.CENTER,
            shadow = textStyle == TextStyle.SHADOW,
            bordered = textStyle == TextStyle.BORDER,
            chroma = chroma
        )
        matrices.pop()
    }

    object TitleLocation : OptionContainer() {
        val BEGINNING = option("Beginning", "Show the title at the start of the text.")
        val END = option("End", "Show the title at the end of text.")
    }

}