/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.SimpleTextElement
import dev.isxander.evergreenhud.utils.decimalFormat
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.OptionContainer
import dev.isxander.settxi.impl.boolean
import dev.isxander.settxi.impl.int
import dev.isxander.settxi.impl.option
import kotlin.math.sqrt

@ElementMeta(id = "evergreenhud:speed", name = "Speed", category = "Player", description = "Display how fast you are moving.")
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

        if (mc.thePlayer != null) {
            val dx = if (useX) mc.thePlayer!!.posX - mc.thePlayer!!.prevPosX else 0.0
            val dy = if (useY) mc.thePlayer!!.posY - mc.thePlayer!!.prevPosY else 0.0
            val dz = if (useZ) mc.thePlayer!!.posZ - mc.thePlayer!!.prevPosZ else 0.0

            // I usually don't leave out whitespaces, but in this case it greatly improved readability
            speed = convertSpeed(sqrt(dx*dx + dy*dy + dz*dz))
        }

        var formattedSpeed = decimalFormat(accuracy, trailingZeros).format(speed)

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
