package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.evergreenhud.utils.decimalFormat
import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Dropdown
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.SingleTextHud
import cc.polyfrost.oneconfig.utils.dsl.mc
import kotlin.math.sqrt

class Speed: Config(Mod("Speed", ModType.HUD), "evergreenhud/speed.json", false) {
    @HUD(name = "Main")
    var hud = SpeedHud()

    init {
        initialize()
    }

    class SpeedHud: SingleTextHud("Speed", true, 0, 110) {

        @Switch(name = "Use X")
        var useX = true

        @Switch(name = "Use Y")
        var useY = true

        @Switch(name = "Use Z")
        var useZ = true

        @Dropdown(
            name = "Speed Unit",
            options = ["Meters per second", "Kilometers per hour", "Miles per hour"],
        )
        var speedUnit = 0

        @Slider(
            name = "Accuracy",
            min = 0F,
            max = 8F
        )
        var accuracy = 2

        @Switch(name = "Trailing Zeros")
        var trailingZeros = true

        @Switch(name = "Suffix")
        var suffix = true

        private fun convertSpeed(speed: Double): Double =
            when (speedUnit) {
                0 -> speed
                1 -> speed * 3.6
                2 -> speed * 2.237
                else -> throw IllegalStateException()
            }

        private val Int.name: String
            get() {
                return when (this) {
                    0 -> "m/s"
                    1 -> "kph"
                    2 -> "mph"
                    else -> throw IllegalStateException()
                }
            }

        override fun getText(example: Boolean): String {
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

    }
}