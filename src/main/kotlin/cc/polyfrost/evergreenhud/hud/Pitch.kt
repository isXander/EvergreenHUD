package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.evergreenhud.utils.decimalFormat
import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.SingleTextHud
import cc.polyfrost.oneconfig.utils.dsl.mc

class Pitch: Config(Mod("Pitch", ModType.HUD), "evergreenhud/pitch.json", false) {
    @HUD(name = "Main")
    var hud = PitchHud()

    init {
        initialize()
    }

    class PitchHud: SingleTextHud("Pitch", true, 180, 50) {

        @Slider(
            name = "Accuracy",
            min = 0F,
            max = 8F
        )
        var accuracy = 2

        @Switch(
            name = "Trailing Zeros"
        )
        var trailingZeros = true

        override fun getText(example: Boolean): String {
            return decimalFormat(accuracy, trailingZeros).format(mc.thePlayer?.rotationPitch ?: 0f )
        }

    }
}