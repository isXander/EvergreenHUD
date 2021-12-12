/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.utils

import java.util.*

// https://stackoverflow.com/a/19759564
private val numerals = sortedMapOf(
    1000 to "M",
    900 to "CM",
    500 to "D",
    400 to "CD",
    100 to "C",
    90 to "XC",
    50 to "L",
    40 to "XL",
    10 to "X",
    9 to "IX",
    5 to "V",
    4 to "IV",
    1 to "I"
) as TreeMap

private val numeralCache = hashMapOf<Int, String>()

fun getRoman(arabic: Int): String =
    numeralCache.computeIfAbsent(arabic) {
        val l = numerals.floorKey(it)
        if (arabic == l) return@computeIfAbsent numerals[it]!!
        return@computeIfAbsent numerals[l]!! + getRoman(it - 1)
    }
