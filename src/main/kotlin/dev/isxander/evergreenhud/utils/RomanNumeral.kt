/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
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
