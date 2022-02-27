/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

val COLOR_CODES = arrayOf("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f")
const val FORMATTING_CODE = '\u00A7'

fun drawStringExt(text: String, x: Number, y: Number, color: Int, shadow: Boolean = true, centered: Boolean = false, bordered: Boolean = false, chroma: Color.ChromaProperties = Color.ChromaProperties.none) = drawStringExt(text, x.toFloat(), y.toFloat(), color, shadow, centered, bordered, chroma)

fun drawStringExt(text: String, x: Float, y: Float, color: Int, shadow: Boolean = true, centered: Boolean = false, bordered: Boolean = false, chroma: Color.ChromaProperties = Color.ChromaProperties.none) {
    when {
        centered -> drawCenteredString(text, x, y, color, shadow, bordered, chroma)
        bordered -> drawBorderedString(text, x, y, color, chroma)
        chroma.hasChroma -> drawChromaString(text, x, y, shadow, chroma)
        shadow -> mc.fontRendererObj.drawStringWithShadow(text, x, y, color)
        else -> mc.fontRendererObj.drawString(text, x.toInt(), y.toInt(), color)
    }
}

private fun drawCenteredString(text: String, x: Float, y: Float, color: Int, shadow: Boolean, bordered: Boolean, chroma: Color.ChromaProperties) {
    drawStringExt(text, x - (mc.fontRendererObj.getStringWidth(text) / 2f), y, color, shadow = shadow, centered = false, bordered = bordered, chroma = chroma)
}

private fun drawBorderedString(text: String, x: Float, y: Float, color: Int, chroma: Color.ChromaProperties) {
    val noCols = stripColorCodes(text)

    drawStringExt(noCols, x + 1, y, 0, shadow = false, centered = false, bordered = false, chroma = Color.ChromaProperties.none)
    drawStringExt(noCols, x - 1, y, 0, shadow = false, centered = false, bordered = false, chroma = Color.ChromaProperties.none)
    drawStringExt(noCols, x, (y + 1f), 0, shadow = false, centered = false, bordered = false, chroma = Color.ChromaProperties.none)
    drawStringExt(noCols, x, (y - 1f), 0, shadow = false, centered = false, bordered = false, chroma = Color.ChromaProperties.none)
    drawStringExt(text, x, y, color, shadow = false, centered = false, bordered = false, chroma = chroma)
}

private fun drawChromaString(text: String, _x: Float, y: Float, shadow: Boolean, chroma: Color.ChromaProperties) {
    var x = _x
    for (char in toCharArrNoFormatting(text)) {
        val i = chroma.getChroma(x, y)
        drawStringExt(char, x, y, i, shadow = shadow)
        x += mc.fontRendererObj.getStringWidth(char)
    }
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

fun stripColorCodes(_text: String, formatCode: String = "$FORMATTING_CODE"): String {
    var text = _text
    for (code in COLOR_CODES) {
        text = text.replace("${formatCode}${code}", "${formatCode}r", ignoreCase = true)
    }
    return text
}
