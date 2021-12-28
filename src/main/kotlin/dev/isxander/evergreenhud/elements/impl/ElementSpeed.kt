/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.OptionContainer
import dev.isxander.settxi.impl.boolean
import dev.isxander.settxi.impl.int
import dev.isxander.settxi.impl.option
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.sqrt

@ElementMeta(id = "SPEED", name = "Speed", category = "Player", description = "Display how fast you are moving.")
class ElementSpeed : SimpleTextElement("Speed") {
    var useX by boolean(true) {
        name = "Use X"
        category = "Speed"
        description = "If the X coordinate is accounted for in the speed calculation."
    }

    var useY by boolean(true) {
        name = "Use Y"
        category = "Speed"
        description = "If the Y coordinate is accounted for in the speed calculation."
    }

    var useZ by boolean(true) {
        name = "Use Z"
        category = "Speed"
        description = "If the Z coordinate is accounted for in the speed calculation."
    }

    var speedUnit by option(SpeedUnit.METERS_PER_SEC) {
        name = "Speed Unit"
        category = "Speed"
        description = "In what unit should the speed be displayed in."
    }

    var accuracy by int(2) {
        range = 0..8
        name = "Accuracy"
        category = "Speed"
        description = "How many decimal places to show the speed to."
    }

    var trailingZeros by boolean(true) {
        name = "Trailing Zeros"
        category = "Speed"
        description = "Show zeros to match the accuracy."
    }

    var suffix by boolean(true) {
        name = "Suffix"
        category = "Speed"
        description = "If the speed value is to be suffixed by your unit (e.g. m/s)"
    }

    override fun calculateValue(): String {
        var speed = 0.0

        if (mc.player != null) {
            val distTraveledLastTickX = if (useX) mc.player!!.x - mc.player!!.prevX else 0.0
            val distTraveledLastTickY = if (useY) mc.player!!.y - mc.player!!.prevY else 0.0
            val distTraveledLastTickZ = if (useZ) mc.player!!.z - mc.player!!.prevZ else 0.0

            speed = convertSpeed(sqrt(distTraveledLastTickX.pow(2) + distTraveledLastTickY.pow(2) + distTraveledLastTickZ.pow(2)))
        }

        val format = if (trailingZeros) "0" else "#"
        var formattedSpeed =
            DecimalFormat("0${if (accuracy > 0) "." else ""}" + format.repeat(accuracy))
            .format(speed)

        if (suffix) formattedSpeed += " ${speedUnit.name}"
        return formattedSpeed
    }

    private fun convertSpeed(speed: Double): Double =
        when (speedUnit) {
            SpeedUnit.METERS_PER_SEC -> speed
            SpeedUnit.KPH -> speed * 3.6
            SpeedUnit.MPH -> speed * 2.237
            else -> throw IllegalStateException()
        }

    object SpeedUnit : OptionContainer() {
        val METERS_PER_SEC = option("m/s", "Meters per second")
        val KPH = option("kph", "Kilometers per hour")
        val MPH = option("mph", "Miles per hour")
    }
}
