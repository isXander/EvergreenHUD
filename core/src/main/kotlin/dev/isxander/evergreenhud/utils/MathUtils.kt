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

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * Linearly interpolates between a and b by t.
 *
 * @param start Start value
 * @param end End value
 * @param interpolation Interpolation between two floats
 * @return interpolated value between a - b
 * @author isXander
 */
fun lerp(start: Float, end: Float, interpolation: Float): Float {
    return start + (end - start) * interpolation
}

/**
 * Returns number between 0 - 1 depending on the range and value given
 *
 * @param num the value
 * @param min minimum of what the value can be
 * @param max maximum of what the value can be
 * @return converted percentage
 * @author isXander
 */
fun getPercent(num: Float, min: Float = 0f, max: Float = 100f): Float {
    return (num - min) / (max - min)
}

/**
 * Returns the percentile of list of longs
 *
 * @param nums the list on which to calculate the percentile
 * @param percentile what percentile the calculation will output
 * @return the percentile of the nums
 * @author isXander
 */
fun List<Long>.percentile(percentile: Double): Long {
    val sorted = this.sorted()
    val index = ceil(percentile / 100.0 * sorted.size).toInt()
    return sorted[index - 1]
}

/**
 * @param num value to change
 * @param places how many decimal places the number should have
 * @return x amount of places of precision of a value
 */
fun precision(num: Float, places: Int): Float {
    val mod = places.coerceAtLeast(0)
    if (places(num) <= mod) return num
    return if (mod == 0) num.roundToInt().toFloat() else (num * mod).roundToInt().toFloat() / mod
}

/**
 * @param num value to check
 * @return the scale of a number
 */
fun places(num: Float): Int {
    return BigDecimal.valueOf(num.toDouble()).scale()
}

/**
 * @param num value to round
 * @param places how many decimal places number should have
 * @return a value with a certain amount of decimal places
 */
fun round(num: Float, places: Int): Float {
    var bd = BigDecimal(num.toString())
    bd = bd.setScale(places, RoundingMode.DOWN)
    return bd.toFloat()
}

fun wrapAngleTo180(_value: Float): Float {
    var value = _value
    value %= 360.0f
    if (value >= 180.0f) {
        value -= 360.0f
    }
    if (value < -180.0f) {
        value += 360.0f
    }
    return value
}