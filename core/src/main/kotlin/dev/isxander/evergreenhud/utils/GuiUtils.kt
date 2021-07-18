package dev.isxander.evergreenhud.utils

import gg.essential.elementa.font.FontProvider
import gg.essential.universal.UMatrixStack
import java.awt.Color
import java.lang.StringBuilder

object GuiUtils {

    val COLOR_CODES = arrayOf("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f")
    val FORMATTING_CODE = '\u00A7'

    fun drawString(matrices: UMatrixStack, font: FontProvider, text: String, x: Float, y: Float, color: Color, shadow: Boolean = true, centered: Boolean, bordered: Boolean = true, chroma: Boolean = true, shadowColor: Color? = null) {
        when {
            bordered -> drawBorderedString(matrices, font, text, x, y, centered, chroma, color)
            chroma -> drawChromaString(matrices, font, text, x, y, shadow, centered)
            centered -> drawCenteredString(matrices, font, text, x, y,  shadow, color, shadowColor)
            else -> font.drawString(matrices, text, color, x, y, 10f, 1f, shadow, shadowColor)
        }
    }

    private fun drawCenteredString(matrices: UMatrixStack, font: FontProvider, text: String, _x: Float, y: Float, shadow: Boolean, color: Color, shadowColor: Color? = null) {
        val x = _x - (font.getStringWidth(text, 10f) / 2f)
        font.drawString(matrices, text, color, x, y, 10f, 1f, shadow, shadowColor)
    }

    private fun drawBorderedString(matrices: UMatrixStack, font: FontProvider, text: String, x: Float, y: Float, centered: Boolean, chroma: Boolean, color: Color) {
        val noCols = stripColorCodes(text)

        drawString(matrices, font, noCols, x + 1, y, Color.black, shadow = false, centered = centered, bordered = false, chroma = chroma)
        drawString(matrices, font, noCols, x - 1, y, Color.black, shadow = false, centered = centered, bordered = false, chroma = chroma)
        drawString(matrices, font, noCols, x, (y + 1f), Color.black, shadow = false, centered = centered, bordered = false, chroma = chroma)
        drawString(matrices, font, noCols, x, (y - 1f), Color.black, shadow = false, centered = centered, bordered = false, chroma = chroma)
        drawString(matrices, font, text, x, y, color, shadow = false, centered = centered, bordered = false, chroma = chroma)
    }

    private fun drawChromaString(matrices: UMatrixStack, font: FontProvider, text: String, _x: Float, y: Float, shadow: Boolean, centered: Boolean) {
        var x = _x
        if (centered) x -= font.getStringWidth(text, 10f) / 2f

        for (char in toCharArrNoFormatting(text)) {
            val i = getChroma(x, y)
            font.drawString(matrices, char, i, x, y, 10f, 1f, shadow)
            x += font.getStringWidth(char, 10f)
        }
    }

    fun getChroma(x: Float, y: Float): Color {
        val v = 2000.0f
        return Color(Color.HSBtoRGB(((System.currentTimeMillis() - x * 10f * 1f - y * 10f * 1f) % v) / v, 0.8f, 0.8f))
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

}