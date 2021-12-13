/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.utils

import net.minecraft.client.util.math.MatrixStack
import java.awt.Color
import java.lang.StringBuilder

val COLOR_CODES = arrayOf("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f")
const val FORMATTING_CODE = '\u00A7'

fun drawString(matrices: MatrixStack, text: String, x: Float, y: Float, color: Int, shadow: Boolean = true, centered: Boolean = false, bordered: Boolean = false, chroma: Boolean = false, chromaSpeed: Float = 2000.0f) {
    when {
        centered -> drawCenteredString(matrices, text, x, y, color, shadow, bordered, chroma, chromaSpeed)
        bordered -> drawBorderedString(matrices, text, x, y, color, chroma, chromaSpeed)
        chroma -> drawChromaString(matrices, text, x, y, shadow, chromaSpeed)
        shadow -> mc.textRenderer.drawWithShadow(matrices, text, x, y, color)
        else -> mc.textRenderer.draw(matrices, text, x, y, color)
    }
}

private fun drawCenteredString(matrices: MatrixStack, text: String, x: Float, y: Float, color: Int, shadow: Boolean, bordered: Boolean, chroma: Boolean, chromaSpeed: Float) {
    drawString(matrices, text, x - (mc.textRenderer.getWidth(text) / 2f), y, color, shadow = shadow, bordered = bordered, chroma = chroma, chromaSpeed = chromaSpeed)
}

private fun drawBorderedString(matrices: MatrixStack, text: String, x: Float, y: Float, color: Int, chroma: Boolean, chromaSpeed: Float) {
    val noCols = stripColorCodes(text)

    drawString(matrices, noCols, x + 1, y, 0, shadow = false, centered = false, bordered = false, chroma = false)
    drawString(matrices, noCols, x - 1, y, 0, shadow = false, centered = false, bordered = false, chroma = false)
    drawString(matrices, noCols, x, (y + 1f), 0, shadow = false, centered = false, bordered = false, chroma = false)
    drawString(matrices, noCols, x, (y - 1f), 0, shadow = false, centered = false, bordered = false, chroma = false)
    drawString(matrices, text, x, y, color, shadow = false, centered = false, bordered = false, chroma = chroma, chromaSpeed = chromaSpeed)
}

private fun drawChromaString(matrices: MatrixStack, text: String, _x: Float, y: Float, shadow: Boolean, speed: Float = 2000.0f) {
    var x = _x
    for (char in toCharArrNoFormatting(text)) {
        val i = getChroma(x, y, speed).rgb
        drawString(matrices, char, x, y, i, shadow = shadow)
        x += mc.textRenderer.getWidth(char)
    }
}

fun getChroma(x: Float, y: Float, speed: Float = 2000.0f): Color {
    return Color(Color.HSBtoRGB(((System.currentTimeMillis() - x * 10f * 1f - y * 10f * 1f) % speed) / speed, 0.8f, 0.8f))
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
