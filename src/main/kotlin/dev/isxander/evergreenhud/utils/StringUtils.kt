/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import java.util.*

val evergreenHudPrefix = LiteralText("[EvergreenHUD] ").formatted(Formatting.GREEN)

operator fun Text.plus(text: Text): MutableText = this.copy().append(text)

val String.localized: String
    get() = TranslatableText(this).string

fun ticksToTime(ticks: Int): String {
    var i = ticks / 20
    val j = i / 60
    i %= 60
    return if (i < 10) "$j:0$i" else "$j:$i"
}

/**
 * Shifts a string a certain number of places
 *
 * @param s string to shift
 * @param amt amount of places to shift
 */
fun shiftString(s: String, amt: Int): String {
    val chars: MutableList<Char> = ArrayList()
    for (c in s.toCharArray()) chars.add(c)
    for (i in 0 until amt) {
        val c = chars[chars.size - 1]
        chars.removeAt(chars.size - 1)
        chars.add(0, c)
    }

    return chars.joinToString()
}

/**
 * Formats bedwars star level into Hypixel's format
 */
fun formatBedwarsStar(stars: Int): String {
    return when {
        stars < 100 -> "[${Formatting.GRAY}[${stars}\u272B]"
        stars < 200 -> "[${Formatting.WHITE}[${stars}\u272B]"
        stars < 300 -> "[${Formatting.GOLD}[${stars}\u272B]"
        stars < 400 -> "[${Formatting.AQUA}[${stars}\u272B]"
        stars < 500 -> "[${Formatting.DARK_GREEN}[${stars}\u272B]"
        stars < 600 -> "[${Formatting.DARK_AQUA}[${stars}\u272B]"
        stars < 700 -> "[${Formatting.DARK_RED}[${stars}\u272B]"
        stars < 800 -> "[${Formatting.LIGHT_PURPLE}[${stars}\u272B]"
        stars < 900 -> "[${Formatting.BLUE}[${stars}\u272B]"
        stars < 1000 -> "[${Formatting.DARK_PURPLE}[${stars}\u272B]"

        else -> {
            val sb = StringBuilder()
            var i = 0
            var forwards = true
            val colors = "c6eabd5"
            for (c in "[$stars\u272B]".toCharArray()) {
                sb.append("\u00a7").append(colors.toCharArray()[i]).append(c)
                if (i >= colors.length - 1) forwards = false else if (i < 1) forwards = true
                if (forwards) i++ else i--
            }
            sb.toString()
        }
    }
}

/**
 * Wraps text according to the font renderer's string width
 *
 * @param text text to wrap
 * @param lineWidth maximum line width
 * @param split word splitter
 * @return wrapped text
 */
fun wrapTextFR(text: String, lineWidth: Int, split: String): String {
    // split with line ending too
    val words = text.split("($split|\n)".toRegex()).toTypedArray()
    // current line width
    var lineLength = 0
    // string concatenation in loop is bad
    val output = StringBuilder()
    for (i in words.indices) {
        var word = words[i]
        // add the word splitter after the word every time except the last word
        if (i != words.size - 1) {
            word += split
        }

        // length of next word
        val wordLength: Int = mc.textRenderer.getWidth(word)
        when {
            lineLength + wordLength <= lineWidth -> { // if the current line length plus this next word is less than the maximum line width
                // if the condition is met, we can just append the word to the current line as it is small enough
                output.append(word)
                lineLength += wordLength
            }
            wordLength <= lineWidth -> { // the word is not big enough to be larger than the whole line max width
                // make a new line before adding the word
                output.append("\n").append(word)
                // the next line has just been made and has been populated with only one word. reset line length and add the word we just added
                lineLength = wordLength
            }
            else -> {
                // the single word will not fit so run the function again with just this word
                // and tell it that every character is it's own word
                output.append(wrapTextFR(word, lineWidth, "")).append(split)
            }
        }
    }
    return output.toString()
}

/**
 * Wraps text using font renderer and adds each line to a list
 *
 * @see wrapTextLinesFR
 */
fun wrapTextLinesFR(text: String, lineWidth: Int, split: String): List<String> {
    val wrapped = wrapTextFR(text, lineWidth, split)
    return if (wrapped == "") {
        ArrayList()
    } else wrapped.split("\n".toRegex())
}

/**
 * Converts 'SOME_ENUM' to 'Some Enum'
 */
fun String.formatEnum(): String {
    return split("_").joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }.trimEnd()
}
