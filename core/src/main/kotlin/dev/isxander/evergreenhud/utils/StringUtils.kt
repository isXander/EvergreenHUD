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
import gg.essential.universal.ChatColor
import java.util.*
import kotlin.collections.ArrayList

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
        stars < 100 -> "[${ChatColor.GRAY}[${stars}\u272B]"
        stars < 200 -> "[${ChatColor.WHITE}[${stars}\u272B]"
        stars < 300 -> "[${ChatColor.GOLD}[${stars}\u272B]"
        stars < 400 -> "[${ChatColor.AQUA}[${stars}\u272B]"
        stars < 500 -> "[${ChatColor.DARK_GREEN}[${stars}\u272B]"
        stars < 600 -> "[${ChatColor.DARK_AQUA}[${stars}\u272B]"
        stars < 700 -> "[${ChatColor.DARK_RED}[${stars}\u272B]"
        stars < 800 -> "[${ChatColor.LIGHT_PURPLE}[${stars}\u272B]"
        stars < 900 -> "[${ChatColor.BLUE}[${stars}\u272B]"
        stars < 1000 -> "[${ChatColor.DARK_PURPLE}[${stars}\u272B]"

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
 * @param str string to check
 * @param times how many times the character has to appear in a string before it is counted as a duplicate
 * @return the amount of duplicate characters in a string
 */
fun hasDuplicateCharacters(str: String, times: Int): Int {
    var repeat = 0
    var index = -1
    var lastChar: Char? = null
    for (c in str.toCharArray()) {
        index++

        if (lastChar == null) {
            lastChar = c
            continue
        }

        if (lastChar == c) repeat++
        else repeat = 0

        if (repeat >= times)
            return index

        lastChar = c
    }
    return -1
}

/**
 * Wraps text according to the character length per line
 *
 * This function is lenient as each line can go above the maximum
 * character length in order to complete a word
 *
 * @param text text to wrap
 * @param charLength the desired character length of each line
 */
fun wrapTextLenient(text: String, charLength: Int): String {
    val sb = StringBuilder()
    var lineLength = 0
    var needsLineBreak = false
    for (c in text.toCharArray()) {
        lineLength += 1
        if (c == '\n') lineLength = 0
        if (lineLength > charLength) {
            needsLineBreak = true
        }
        if (needsLineBreak && c == ' ') {
            lineLength = 0
            sb.append('\n')
            needsLineBreak = false
        } else {
            sb.append(c)
        }
    }
    return sb.toString()
}

/**
 * Wraps text according to the font renderer's string width
 *
 * @param text text to wrap
 * @param fontRenderer font renderer that measures string width
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
        // add a the word splitter after the word every time except the last word
        if (i != words.size - 1) {
            word += split
        }

        // length of next word
        val wordLength: Int = FONT_RENDERER.width(word)
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
 * @see StringUtils.wrapTextLinesFR
 */
fun wrapTextLinesFR(text: String, lineWidth: Int, split: String): List<String> {
    val wrapped = wrapTextFR(text, lineWidth, split)
    return if (wrapped == "") {
        ArrayList()
    } else wrapped.split("\n".toRegex())
}

/**
 * Counts how many times text appears in a string
 *
 * @param text string to check
 * @param toCheck what to check for
 */
fun count(text: String, toCheck: String): Int {
    var count = 0
    var i = 0
    while (i < text.length) {
        if (text.substring(i).startsWith(toCheck)) {
            count++
            i += toCheck.length - 1
        }
        i++
    }
    return count
}

/**
 * Makes the first character of a string upper-case
 *
 * @param original original text
 */
fun firstUpper(original: String): String {
    return if (original.length == 1) original.uppercase() else original.substring(0, 1)
        .uppercase() + original.substring(1).lowercase()
}

/**
 * Repeats text some amount of times
 * Backported from later Java versions
 *
 * @param text text to repeat
 * @param amount amount of times to repeat
 */
fun repeat(text: String?, amount: Int): String {
    val sb = StringBuilder()
    for (i in 0..amount) {
        sb.append(text)
    }
    return sb.toString()
}

/**
 * Converts 'SOME_ENUM' to 'Some Enum'
 */
fun capitalizeEnum(`in`: String): String {
    val out = `in`.lowercase()
    var lastSpace = true
    val chars: MutableList<String> = ArrayList()
    for (c in out.toCharArray()) {
        chars.add(c.toString() + "")
    }
    for (i in chars.indices) {
        val c = chars[i]
        if (lastSpace) {
            chars[i] = c.uppercase()
        }
        lastSpace = c == " "
    }
    val sb = StringBuilder()
    for (s in chars) sb.append(s)
    return sb.toString()
}