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

package dev.isxander.evergreenhud.utils

import dev.isxander.evergreenhud.compatibility.universal.FONT_RENDERER
import gg.essential.elementa.font.FontProvider
import gg.essential.universal.UMatrixStack
import java.awt.Color
import java.lang.StringBuilder

val COLOR_CODES = arrayOf("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f")
val FORMATTING_CODE = '\u00A7'

fun drawString(text: String, x: Float, y: Float, color: Int, shadow: Boolean = true, centered: Boolean, bordered: Boolean = true, chroma: Boolean = true, chromaSpeed: Float = 2000.0f) {
    when {
        bordered -> drawBorderedString(text, x, y, centered, chroma, color)
        chroma -> drawChromaString(text, x, y, shadow, centered, chromaSpeed)
        centered -> drawCenteredString(text, x, y, shadow, color)
        else -> FONT_RENDERER.draw(text, x, y, color, shadow)
    }
}

private fun drawCenteredString(text: String, _x: Float, y: Float, shadow: Boolean, color: Int) {
    val x = _x - (FONT_RENDERER.width(text) / 2f)
    FONT_RENDERER.draw(text, x, y, color, shadow)
}

private fun drawBorderedString(text: String, x: Float, y: Float, centered: Boolean, chroma: Boolean, color: Int) {
    val noCols = stripColorCodes(text)

    drawString(noCols, x + 1, y, 0, shadow = false, centered = centered, bordered = false, chroma = chroma)
    drawString(noCols, x - 1, y, 0, shadow = false, centered = centered, bordered = false, chroma = chroma)
    drawString(noCols, x, (y + 1f), 0, shadow = false, centered = centered, bordered = false, chroma = chroma)
    drawString(noCols, x, (y - 1f), 0, shadow = false, centered = centered, bordered = false, chroma = chroma)
    drawString(text, x, y, color, shadow = false, centered = centered, bordered = false, chroma = chroma)
}

private fun drawChromaString(text: String, _x: Float, y: Float, shadow: Boolean, centered: Boolean, speed: Float = 2000.0f) {
    var x = _x
    if (centered) x -= FONT_RENDERER.width(text) / 2f

    for (char in toCharArrNoFormatting(text)) {
        val i = getChroma(x, y, speed).rgb
        FONT_RENDERER.draw(char, x, y, i, shadow)
        x += FONT_RENDERER.width(char)
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