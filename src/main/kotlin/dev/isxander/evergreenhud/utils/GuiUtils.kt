/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import gg.essential.universal.shader.BlendState
import gg.essential.universal.shader.UShader
import net.minecraft.client.resource.language.I18n
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.*
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

val COLOR_CODES = arrayOf("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f")
const val FORMATTING_CODE = '\u00A7'

fun drawString(matrices: MatrixStack, text: Text, x: Float, y: Float, color: Int, shadow: Boolean = true, centered: Boolean = false, bordered: Boolean = false, chroma: Color.ChromaProperties = Color.ChromaProperties.none) {
    when {
        centered -> drawCenteredString(matrices, text, x, y, color, shadow, bordered, chroma)
        bordered -> drawBorderedString(matrices, text, x, y, color, chroma)
        chroma.hasChroma -> drawChromaString(matrices, text, x, y, shadow, chroma)
        shadow -> mc.textRenderer.drawWithShadow(matrices, text, x, y, color)
        else -> mc.textRenderer.draw(matrices, text, x, y, color)
    }
}

fun drawString(matrices: MatrixStack, text: String, x: Float, y: Float, color: Int, shadow: Boolean = true, centered: Boolean = false, bordered: Boolean = false, chroma: Color.ChromaProperties = Color.ChromaProperties.none) {
    drawString(matrices, LiteralText(text), x, y, color, shadow, centered, bordered, chroma)
}

private fun drawCenteredString(matrices: MatrixStack, text: Text, x: Float, y: Float, color: Int, shadow: Boolean, bordered: Boolean, chroma: Color.ChromaProperties) {
    drawString(matrices, text, x - (mc.textRenderer.getWidth(text) / 2f), y, color, shadow = shadow, centered = false, bordered = bordered, chroma = chroma)
}

private fun drawBorderedString(matrices: MatrixStack, text: Text, x: Float, y: Float, color: Int, chroma: Color.ChromaProperties) {
    val noCols = text.modify { stripColorCodes(it) }

    drawString(matrices, noCols, x + 1, y, 0, shadow = false, centered = false, bordered = false, chroma = Color.ChromaProperties.none)
    drawString(matrices, noCols, x - 1, y, 0, shadow = false, centered = false, bordered = false, chroma = Color.ChromaProperties.none)
    drawString(matrices, noCols, x, (y + 1f), 0, shadow = false, centered = false, bordered = false, chroma = Color.ChromaProperties.none)
    drawString(matrices, noCols, x, (y - 1f), 0, shadow = false, centered = false, bordered = false, chroma = Color.ChromaProperties.none)
    drawString(matrices, text, x, y, color, shadow = false, centered = false, bordered = false, chroma = chroma)
}

private fun drawChromaString(matrices: MatrixStack, text: Text, _x: Float, y: Float, shadow: Boolean, chroma: Color.ChromaProperties) {
    var x = _x
    for (char in toCharArrNoFormatting(text.extractString())) {
        val i = chroma.getChroma(x, y)
        drawString(matrices, LiteralText(char), x, y, i, shadow = shadow)
        x += mc.textRenderer.getWidth(char)}
}

fun toCharArrNoFormatting(text: String): ArrayList<String> {
    val split = arrayListOf<String>()
    val format = StringBuilder()
    var next = false

    val arr = text.toCharArray()
    for (i in arr.indices) {
        if (next) {
            next = false
            continue
        }

        val c = arr[i]

        if (i != arr.size - 1 && c == FORMATTING_CODE) {
            val nextChar = arr[i + 1]

            // reset formatting so just clear the str builder
            if (nextChar == 'r') format.setLength(0)
            else format.append(FORMATTING_CODE).append(nextChar)

            next = true
            continue
        }

        split.add("${format}${c}")
    }

    return split
}

fun stripFormattingCodes(_text: String, formatCode: String = "$FORMATTING_CODE"): String {
    var text = _text
    while (text.contains(formatCode)) {
        text = text.replaceFirst(formatCode + text[text.indexOf(formatCode) + 1], "")
    }
    return text
}

fun stripColorCodes(_text: String, formatCode: String = "$FORMATTING_CODE"): String {
    var text = _text
    for (code in COLOR_CODES) {
        text = text.replace("${formatCode}${code}", "${formatCode}r", ignoreCase = true)
    }
    return text
}

fun Text.extractString(): String {
    return when (this) {
        is TranslatableText -> I18n.translate(key, args)
        is KeybindText -> I18n.translate(key)
        is LiteralText -> this.string
        else -> throw UnsupportedOperationException("ScoreText is not supported")
    }
}

fun Text.modify(lambda: (String) -> String): Text {
    val string = extractString().run(lambda)
    return LiteralText(string).fillStyle(this.style)
}

operator fun Formatting.plus(string: String): String =
    this.toString() + string

fun UShader.Companion.readFromLegacyShader(shader: Identifier, blendState: BlendState = BlendState.NORMAL): UShader =
    fromLegacyShader(
        Identifier(shader.namespace, shader.path + ".vsh").readText(),
        Identifier(shader.namespace, shader.path + ".fsh").readText(),
        blendState
    )
